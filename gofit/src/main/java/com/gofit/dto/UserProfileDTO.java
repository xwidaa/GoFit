package com.gofit.dto;

import java.time.LocalDate;

public class UserProfileDTO {
    private String email;
    private double height;
    private double weight;
    private String activityLevel;
    private String goal;
    private int age;
    private double bmi;
    private int dailyCalories;
    private String gender;
    private String firstName;
    private String lastName;
    private String role;
    // 🔥 Câmpul nou adăugat pentru a repara problema vârstei
    private LocalDate birthDate;

    // 1. Constructorul complet (Acum are 13 argumente)
    public UserProfileDTO(String email, double height, double weight, String activityLevel,
                          String goal, int age, double bmi, int dailyCalories,
                          String gender, String firstName, String lastName, String role,
                          LocalDate birthDate) {
        this.email = email;
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.age = age;
        this.bmi = bmi;
        this.dailyCalories = dailyCalories;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.birthDate = birthDate;
    }

    // Constructor gol (necesar pentru Jackson/Spring)
    public UserProfileDTO() {
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getBmi() { return bmi; }
    public void setBmi(double bmi) { this.bmi = bmi; }

    public int getDailyCalories() { return dailyCalories; }
    public void setDailyCalories(int dailyCalories) { this.dailyCalories = dailyCalories; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
}