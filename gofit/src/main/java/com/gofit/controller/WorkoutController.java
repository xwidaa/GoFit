package com.gofit.controller;

import com.gofit.model.Workout;
import com.gofit.repository.WorkoutRepository;
import com.gofit.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.gofit.model.WorkoutTemplate;
import com.gofit.repository.WorkoutTemplateRepository;
import com.gofit.repository.UserRepository;
import java.util.stream.Collectors;
import com.gofit.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors; // De asemenea, asigură-te că ai și asta pentru .stream()
@RestController
@RequestMapping("/workouts")
@CrossOrigin
public class WorkoutController {

    private final WorkoutRepository workoutRepository;
    private final WorkoutTemplateRepository templateRepository; // NOU
    private final UserRepository userRepository; // NOU pentru a lua Goal-ul
    private final JwtService jwtService;

    public WorkoutController(WorkoutRepository workoutRepository,
                             WorkoutTemplateRepository templateRepository,
                             UserRepository userRepository,
                             JwtService jwtService) {
        this.workoutRepository = workoutRepository;
        this.templateRepository = templateRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @GetMapping(value = "/plan", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getRecommendedPlan(@RequestHeader("Authorization") String authHeader) {
        try {
            String email = extractEmail(authHeader);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String userGoal = user.getGoal().name();
            long userId = user.getId();

            // 1. Luăm TOATE exercițiile din baza de date pentru acest Goal specific
            List<WorkoutTemplate> allTemplates = templateRepository.findByTargetGoal(userGoal);

            // 2. Definim ordinea zilelor
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

            // 3. Calculăm "Shift-ul" (decalajul) bazat pe ID
            // % 7 înseamnă că avem 7 variații posibile de program
            int shift = (int) (userId % 7);

            Map<String, List<WorkoutTemplate>> personalizedPlan = new HashMap<>();

            for (int i = 0; i < days.length; i++) {
                // Ziua în care utilizatorul vede exercițiul
                String displayDay = days[i];

                // Ziua din baza de date de unde luăm exercițiul (decalată)
                int sourceIndex = (i + shift) % days.length;
                String sourceDay = days[sourceIndex];

                List<WorkoutTemplate> dayExercises = allTemplates.stream()
                        .filter(t -> t.getDayOfWeek().equalsIgnoreCase(sourceDay))
                        .collect(Collectors.toList());

                personalizedPlan.put(displayDay, dayExercises);
            }

            return ResponseEntity.ok(personalizedPlan);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error: " + e.getMessage());
        }
    }

    @GetMapping(value = "/today", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getTodayWorkouts(@RequestHeader("Authorization") String authHeader) {
        try {
            String email = extractEmail(authHeader);

            List<Workout> workouts = workoutRepository
                    .findByUserEmailAndDateOrderByDateDesc(email, LocalDate.now());

            return ResponseEntity.ok(workouts);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error: " + e.getMessage());
        }
    }

    @PostMapping(produces = "application/json;charset=UTF-8")
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

    @DeleteMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> deleteWorkout(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable Long id) {
        try {
            // 1. Extragem email-ul din token pentru securitate
            String email = extractEmail(authHeader);

            // 2. Căutăm workout-ul în baza de date
            Workout w = workoutRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Workout not found"));

            // 3. VERIFICARE CRITICĂ: Doar proprietarul poate șterge
            if (!w.getUserEmail().equals(email)) {
                return ResponseEntity.status(403).body("You can only delete your own workouts!");
            }

            // 4. Dacă totul e ok, ștergem
            workoutRepository.delete(w);
            return ResponseEntity.ok("Deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    private String extractEmail(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtService.extractEmail(token);
    }
}
