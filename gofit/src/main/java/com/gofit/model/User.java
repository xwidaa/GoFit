package com.gofit.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String firstName;
    private String lastName;
    private double height;
    private double weight;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    // 🔥 AICI ESTE CHEIA
    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private Subscription subscription;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public User() {}

    public User(Long id, String email, String password, Role role,
                double height, double weight, LocalDate birthDate,
                ActivityLevel activityLevel, Goal goal) {
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

    // ───── GETTERS & SETTERS ─────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public Subscription getSubscription() { return subscription; }
    public void setSubscription(Subscription subscription) { this.subscription = subscription; }

    // 🔥 Calories logic (unchanged)
    public int getDailyCalories() {
        int age = java.time.Period.between(this.birthDate, java.time.LocalDate.now()).getYears();

        double bmr;
        if (Gender.MALE.equals(this.gender)) {
            bmr = 10 * this.weight + 6.25 * this.height - 5 * age + 5;
        } else {
            bmr = 10 * this.weight + 6.25 * this.height - 5 * age - 161;
        }

        double activityMultiplier = switch (this.activityLevel) {
            case SEDENTARY -> 1.2;
            case LIGHT     -> 1.375;
            case MODERATE  -> 1.55;
            case ACTIVE    -> 1.725;
            case ATHLETE   -> 1.9;
        };

        int maintenance = (int) (bmr * activityMultiplier);

        return switch (this.goal) {
            case LOSE_WEIGHT   -> maintenance - 500;
            case GAIN_WEIGHT   -> maintenance + 500;
            case BUILD_MUSCLE  -> maintenance + 300;
            default            -> maintenance;
        };
    }
}