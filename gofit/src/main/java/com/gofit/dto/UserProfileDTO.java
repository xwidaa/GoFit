package com.gofit.dto;

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

    // Standard constructor that matches the UserController call
    public UserProfileDTO(String email, double height, double weight, String activityLevel,
                          String goal, int age, double bmi, int dailyCalories,
                          String gender, String firstName, String lastName) {
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
    }

    // Getters and Setters...
    public String getEmail() { return email; }
    public double getHeight() { return height; }
    public double getWeight() { return weight; }
    public String getActivityLevel() { return activityLevel; }
    public String getGoal() { return goal; }
    public int getAge() { return age; }
    public double getBmi() { return bmi; }
    public int getDailyCalories() { return dailyCalories; }
    public String getGender() { return gender; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}