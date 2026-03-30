package com.gofit.repository;

import com.gofit.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDate;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserEmailOrderByDateDesc(String userEmail);
    List<Workout> findByUserEmailAndDateOrderByDateDesc(String userEmail, LocalDate date);
}
