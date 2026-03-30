package com.gofit.repository;

import com.gofit.model.WorkoutTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkoutTemplateRepository extends JpaRepository<WorkoutTemplate, Long> {
    List<WorkoutTemplate> findByTargetGoal(String targetGoal);
}