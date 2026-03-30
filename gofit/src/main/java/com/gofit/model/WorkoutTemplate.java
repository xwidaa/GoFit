package com.gofit.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class WorkoutTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // ex: RDLs
    private String setsReps;    // ex: 3 sets x 15 reps
    private String videoUrl;    // Link YouTube
    private String targetGoal;  // LOSE_WEIGHT, GAIN_MUSCLE, etc.
    private String dayOfWeek;   // Monday, Tuesday, etc.
    private String muscleGroup; // Legs, Chest, etc.
}