package com.gofit.controller;

import com.gofit.model.Goal;
import com.gofit.model.Meal;
import com.gofit.model.User;
import com.gofit.repository.MealRepository;
import com.gofit.service.NutritionService;
import com.gofit.service.UserService;
import com.gofit.security.JwtService; // Importul e bun acum
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/nutrition")
@CrossOrigin
public class NutritionController {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NutritionService nutritionService;

    @Autowired // ACEASTA ESTE LINIA CARE LIPSEA
    private JwtService jwtService; // ACEASTA ESTE LINIA CARE LIPSEA

    @GetMapping("/plan")
    public ResponseEntity<?> getMyPlan(@RequestHeader("Authorization") String authHeader) {
        try {
            // Curățăm token-ul
            String token = authHeader.replace("Bearer ", "");

            // Acum jwtService va fi recunoscut pentru că l-am declarat mai sus cu @Autowired
            String email = jwtService.extractEmail(token);

            User user = userService.findByEmail(email);

            Goal userGoalEnum = user.getGoal();

            List<Meal> allMeals = mealRepository.findByTargetGoal(userGoalEnum);

            if (allMeals.isEmpty()) {
                nutritionService.generatePlanForUser(user);
                allMeals = mealRepository.findByTargetGoal(userGoalEnum);
            }

            Map<String, List<Meal>> planByDays = allMeals.stream()
                    .collect(Collectors.groupingBy(Meal::getDayOfWeek));

            return ResponseEntity.ok(planByDays);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error in Java: " + e.getMessage());
        }
    }
}