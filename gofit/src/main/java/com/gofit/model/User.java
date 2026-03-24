package com.gofit.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    private double height;
    private double weight;

    private LocalDate birthDate; // 🔥 folosim asta

    private String activityLevel;
    private String goal;

    private String role;

    // constructor gol (obligatoriu)
    public User() {}

    // constructor corect (fără age)
    public User(Long id, String email, String password, String role,
                double height, double weight, LocalDate birthDate,
                String activityLevel, String goal) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.height = height;
        this.weight = weight;
        this.birthDate = birthDate;
        this.activityLevel = activityLevel;
        this.goal = goal;
    }

    // getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}