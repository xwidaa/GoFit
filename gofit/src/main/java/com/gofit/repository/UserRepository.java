package com.gofit.repository;

import com.gofit.model.Goal;
import com.gofit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // 1. Get the most recent users for the "Recent Users" list
    List<User> findTop5ByOrderByCreatedAtDesc();

    // 2. Count users who joined after a certain date (for the "+12 this week" stat)
    long countByCreatedAtAfter(LocalDateTime date);

    // 3. Get distribution of goals (JPQL query to get Goal name and count)
    @Query("SELECT u.goal as goal, COUNT(u) as count FROM User u GROUP BY u.goal")
    List<Object[]> countUsersByGoal();

    // 4. Get distribution of activity levels
    @Query("SELECT u.activityLevel as level, COUNT(u) as count FROM User u GROUP BY u.activityLevel")
    List<Object[]> countUsersByActivityLevel();
}