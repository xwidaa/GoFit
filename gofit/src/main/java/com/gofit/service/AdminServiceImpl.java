package com.gofit.service;

import com.gofit.dto.AdminDashboardDTO;
import com.gofit.dto.UserDTO;
import com.gofit.model.User;
import com.gofit.repository.UserRepository;
import com.gofit.repository.WorkoutRepository;
import com.gofit.repository.MealRepository;
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

    public AdminServiceImpl(UserRepository userRepository,
                            WorkoutRepository workoutRepository,
                            MealRepository mealRepository) {
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
        this.mealRepository = mealRepository;
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
}