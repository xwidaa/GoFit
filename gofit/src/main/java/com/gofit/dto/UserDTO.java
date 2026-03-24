package com.gofit.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.gofit.model.ActivityLevel;
import com.gofit.model.Goal;

public class UserDTO {

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Min(value = 100, message = "Height too small")
    @Max(value = 250, message = "Height too big")
    private double height;

    @Min(value = 30, message = "Weight too small")
    @Max(value = 300, message = "Weight too big")
    private double weight;

    @NotNull(message = "Birth date required")
    private LocalDate birthDate;

    @NotNull(message = "Activity level required")
    private ActivityLevel activityLevel;

    @NotNull(message = "Goal required")
    private Goal goal;

    // getters + setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }
}