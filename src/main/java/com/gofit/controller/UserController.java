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

            // BMI = weight(kg) / (height(m))^2
            double heightM = user.getHeight() / 100.0;
            double bmi = Math.round((user.getWeight() / (heightM * heightM)) * 10.0) / 10.0;


            double bmr;

            if (user.getGender().name().equals("MALE")) {
                bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * age + 5;
            } else {
                bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * age - 161;
            }

            double activityMultiplier = switch (user.getActivityLevel()) {
                case "SEDENTARY" -> 1.2;
                case "LIGHT"     -> 1.375;
                case "MODERATE"  -> 1.55;
                case "ACTIVE"    -> 1.725;
                case "ATHLETE"   -> 1.9;
                default          -> 1.4;
            };

            int maintenance = (int) (bmr * activityMultiplier);

            int dailyCalories = switch (user.getGoal()) {
                case "LOSE_WEIGHT"   -> maintenance - 500;
                case "GAIN_WEIGHT"   -> maintenance + 500;
                case "BUILD_MUSCLE"  -> maintenance + 300;
                default              -> maintenance;
            };

            UserProfileDTO profile = new UserProfileDTO(
                    user.getEmail(),
                    user.getHeight(),
                    user.getWeight(),
                    user.getActivityLevel(),
                    user.getGoal(),
                    age,
                    bmi,
                    dailyCalories,
                    user.getGender().name(),
                    user.getFirstName(),
                    user.getLastName());

            return ResponseEntity.ok(profile);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }
}
