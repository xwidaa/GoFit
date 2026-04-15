package com.gofit.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.gofit.model.ActivityLevel;
import com.gofit.model.Goal;
import com.gofit.model.Gender;

public class UserDTO {

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "First name required")
    private String firstName;

    @NotBlank(message = "Last name required")
    private String lastName;

    @Min(value = 100, message = "Height too small")
    @Max(value = 250, message = "Height too big")
    private double height;

    @Min(value = 30, message = "Weight too small")
    @Max(value = 300, message = "Weight too big")
    private double weight;

    @NotNull(message = "Birth date required")
    private LocalDate birthDate;

    @NotNull(message = "Gender required")
    private Gender gender;

    @NotNull(message = "Activity level required")
    private ActivityLevel activityLevel;

    @NotNull(message = "Goal required")
    private Goal goal;

    private String role;

    // 🔥 Câmpuri noi pentru calcul
    private int age;
    private double bmi;
    private int dailyCalories;

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public ActivityLevel getActivityLevel() { return activityLevel; }
    public void setActivityLevel(ActivityLevel activityLevel) { this.activityLevel = activityLevel; }

    public Goal getGoal() { return goal; }
    public void setGoal(Goal goal) { this.goal = goal; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // 🔥 Getters și Setters noi
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getBmi() { return bmi; }
    public void setBmi(double bmi) { this.bmi = bmi; }

    public int getDailyCalories() { return dailyCalories; }
    public void setDailyCalories(int dailyCalories) { this.dailyCalories = dailyCalories; }
}