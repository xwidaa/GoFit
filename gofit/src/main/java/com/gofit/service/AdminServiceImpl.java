package com.gofit.service;

import com.gofit.dto.AdminDashboardDTO;
import com.gofit.dto.UserDTO;
import com.gofit.model.User;
import com.gofit.repository.UserRepository;
import com.gofit.repository.WorkoutRepository;
import com.gofit.repository.MealRepository;
import com.gofit.model.Role;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final MealRepository mealRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(UserRepository userRepository,
                            WorkoutRepository workoutRepository,
                            MealRepository mealRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
        this.mealRepository = mealRepository;
        this.passwordEncoder = passwordEncoder;
    }

    

    @Override
    public List<User> getAllUsers() {
        // This pulls every single user from the database
        return userRepository.findAll();
    }

    @Override
    public AdminDashboardDTO getDashboardStats() {
        AdminDashboardDTO stats = new AdminDashboardDTO();

        // 1. Basic Counts
        stats.setTotalUsers(userRepository.count());
        stats.setTotalWorkouts(workoutRepository.count());
        stats.setTotalNutritionPlans(mealRepository.count());

        // 2. Growth Stats (Users joined in the last 7 days)
        LocalDateTime lastWeek = LocalDateTime.now().minusDays(7);
        stats.setUsersThisWeek(userRepository.countByCreatedAtAfter(lastWeek));

        // 3. Goal Distribution (Mapping DB List<Object[]> to Map<String, Long>)
        Map<String, Long> goalMap = new HashMap<>();
        userRepository.countUsersByGoal().forEach(row -> {
            goalMap.put(row[0].toString(), (Long) row[1]);
        });
        stats.setUsersByGoal(goalMap);

        // 4. Activity Distribution
        Map<String, Long> activityMap = new HashMap<>();
        userRepository.countUsersByActivityLevel().forEach(row -> {
            activityMap.put(row[0].toString(), (Long) row[1]);
        });
        stats.setUsersByActivity(activityMap);

        // 5. Recent Users (Mapping Entity to DTO)
        List<UserDTO> recentUsers = userRepository.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        stats.setRecentUsers(recentUsers);

        // Note: For "Active Subscriptions", you'd call a SubscriptionRepository
        // once you've implemented that model. For now, we can leave it at 0.
        stats.setActiveSubscriptions(0);

        return stats;
    }

    // Helper to map User to UserDTO (consistent with your register logic)
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setGoal(user.getGoal());
        // Add birthDate or joinDate as needed for your table
        return dto;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());

        // 🔹 EMAIL (verificare duplicat)
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {

            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }

            user.setEmail(dto.getEmail());
        }

        // 🔹 PAROLA (DOAR dacă este completată)
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getWeight() != null) user.setWeight(dto.getWeight());
        if (dto.getHeight() != null) user.setHeight(dto.getHeight());

        if (dto.getGoal() != null) user.setGoal(dto.getGoal());
        if (dto.getActivityLevel() != null) user.setActivityLevel(dto.getActivityLevel());

        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getBirthDate() != null) user.setBirthDate(dto.getBirthDate());

        if (dto.getRole() != null) {
            user.setRole(Enum.valueOf(com.gofit.model.Role.class, dto.getRole()));
        }

        userRepository.save(user);
    }

    @Override
    public void createUser(UserDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setEmail(dto.getEmail());

        // 🔥 IMPORTANT
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        user.setWeight(dto.getWeight());
        user.setHeight(dto.getHeight());

        user.setGoal(dto.getGoal());
        user.setActivityLevel(dto.getActivityLevel());
        user.setGender(dto.getGender());
        user.setBirthDate(dto.getBirthDate());

        user.setRole(Role.valueOf(dto.getRole())); // USER / ADMIN

        userRepository.save(user);
    }

}