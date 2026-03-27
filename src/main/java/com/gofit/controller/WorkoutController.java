package com.gofit.controller;

import com.gofit.model.Workout;
import com.gofit.repository.WorkoutRepository;
import com.gofit.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workouts")
@CrossOrigin
public class WorkoutController {

    private final WorkoutRepository workoutRepository;
    private final JwtService jwtService;

    public WorkoutController(WorkoutRepository workoutRepository, JwtService jwtService) {
        this.workoutRepository = workoutRepository;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<?> getWorkouts(@RequestHeader("Authorization") String authHeader) {
        try {
            String email = extractEmail(authHeader);
            List<Workout> workouts = workoutRepository.findByUserEmailOrderByDateDesc(email);
            return ResponseEntity.ok(workouts);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }

    @PostMapping
    public ResponseEntity<?> addWorkout(@RequestHeader("Authorization") String authHeader,
                                        @RequestBody Map<String, Object> body) {
        try {
            String email = extractEmail(authHeader);

            Workout w = new Workout();
            w.setUserEmail(email);
            w.setName((String) body.get("name"));
            w.setType((String) body.get("type"));
            w.setDurationMinutes((Integer) body.get("durationMinutes"));
            w.setCaloriesBurned((Integer) body.get("caloriesBurned"));
            w.setNotes((String) body.getOrDefault("notes", ""));
            w.setDate(LocalDate.now());

            workoutRepository.save(w);
            return ResponseEntity.ok(w);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkout(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable Long id) {
        try {
            String email = extractEmail(authHeader);
            Workout w = workoutRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Not found"));
            if (!w.getUserEmail().equals(email)) {
                return ResponseEntity.status(403).body("Forbidden");
            }
            workoutRepository.delete(w);
            return ResponseEntity.ok("Deleted");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    private String extractEmail(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtService.extractEmail(token);
    }
}
