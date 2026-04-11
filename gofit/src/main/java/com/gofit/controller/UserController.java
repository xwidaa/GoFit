package com.gofit.controller;

import com.gofit.dto.UserProfileDTO;
import com.gofit.model.User;
import com.gofit.repository.UserRepository;
import com.gofit.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtService.extractEmail(token);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            int age = Period.between(user.getBirthDate(), LocalDate.now()).getYears();
            double heightM = user.getHeight() / 100.0;
            double bmi = Math.round((user.getWeight() / (heightM * heightM)) * 10.0) / 10.0;

            int dailyCalories = user.getDailyCalories();

            // 🔥 Constructor call matches UserProfileDTO exactly now
            UserProfileDTO profile = new UserProfileDTO(
                    user.getEmail(),
                    user.getHeight(),
                    user.getWeight(),
                    user.getActivityLevel().name(),
                    user.getGoal().name(),
                    age,
                    bmi,
                    dailyCalories,
                    user.getGender().name(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRole().name() // 🔥 ADAUGĂ ACEASTĂ LINIE LA FINAL
            );

            return ResponseEntity.ok(profile);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());
        }
    }
}