package com.gofit.repository;

import com.gofit.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserEmailOrderByDateDesc(String userEmail);
}
