package com.gofit.dto;

import java.util.List;
import java.util.Map;

public class AdminDashboardDTO {

    // Main Stat Cards
    private long totalUsers;
    private long usersThisWeek;
    private long activeSubscriptions;
    private long subscriptionsThisMonth;
    private long totalWorkouts;
    private long totalNutritionPlans;

    // Charts & Lists
    private Map<String, Long> usersByGoal;      // For the "Users by goal" chart
    private Map<String, Long> usersByActivity;  // For the "Activity level" chart
    private List<UserDTO> recentUsers;          // For the "Recent users" table

    public AdminDashboardDTO() {}

    // --- Getters and Setters ---

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getUsersThisWeek() { return usersThisWeek; }
    public void setUsersThisWeek(long usersThisWeek) { this.usersThisWeek = usersThisWeek; }

    public long getActiveSubscriptions() { return activeSubscriptions; }
    public void setActiveSubscriptions(long activeSubscriptions) { this.activeSubscriptions = activeSubscriptions; }

    public long getSubscriptionsThisMonth() { return subscriptionsThisMonth; }
    public void setSubscriptionsThisMonth(long subscriptionsThisMonth) { this.subscriptionsThisMonth = subscriptionsThisMonth; }

    public long getTotalWorkouts() { return totalWorkouts; }
    public void setTotalWorkouts(long totalWorkouts) { this.totalWorkouts = totalWorkouts; }

    public long getTotalNutritionPlans() { return totalNutritionPlans; }
    public void setTotalNutritionPlans(long totalNutritionPlans) { this.totalNutritionPlans = totalNutritionPlans; }

    public Map<String, Long> getUsersByGoal() { return usersByGoal; }
    public void setUsersByGoal(Map<String, Long> usersByGoal) { this.usersByGoal = usersByGoal; }

    public Map<String, Long> getUsersByActivity() { return usersByActivity; }
    public void setUsersByActivity(Map<String, Long> usersByActivity) { this.usersByActivity = usersByActivity; }

    public List<UserDTO> getRecentUsers() { return recentUsers; }
    public void setRecentUsers(List<UserDTO> recentUsers) { this.recentUsers = recentUsers; }
}