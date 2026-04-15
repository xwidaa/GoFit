package com.gofit.service;

import com.gofit.dto.UserDTO;
import com.gofit.dto.UserProfileDTO;
import com.gofit.model.*;
import com.gofit.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) throw new RuntimeException("User does not exist");
        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) throw new RuntimeException("Wrong password");
        return user.getEmail();
    }

    @Override
    @Transactional
    public void register(UserDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) throw new RuntimeException("Email already exists");
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setHeight(dto.getHeight());
        user.setWeight(dto.getWeight());
        user.setBirthDate(dto.getBirthDate());
        user.setGender(dto.getGender());
        user.setActivityLevel(dto.getActivityLevel());
        user.setGoal(dto.getGoal());
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDTO getUserDetails(String email) {
        User user = findByEmail(email);

        // 🔥 CALCUL VÂRSTĂ
        int age = 0;
        if (user.getBirthDate() != null) {
            age = Period.between(user.getBirthDate(), LocalDate.now()).getYears();
        }

        // 🔥 CALCUL BMI
        double bmi = 0;
        if (user.getHeight() > 0) {
            double heightM = user.getHeight() / 100.0;
            bmi = user.getWeight() / (heightM * heightM);
            bmi = Math.round(bmi * 10.0) / 10.0; // o zecimală
        }

        // 🔥 CALCUL CALORII (Mifflin-St Jeor)
        double bmr = (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * age);
        bmr = (user.getGender() == Gender.MALE) ? bmr + 5 : bmr - 161;

        // Multiplicator activitate
        double multiplier = switch (user.getActivityLevel()) {
            case SEDENTARY -> 1.2;
            case LIGHT -> 1.375;
            case MODERATE -> 1.55;
            case ACTIVE -> 1.725;
            default -> 1.2;
        };
        int calories = (int) Math.round(bmr * multiplier);

        // Mapare către DTO
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setHeight(user.getHeight());
        dto.setWeight(user.getWeight());
        dto.setBirthDate(user.getBirthDate());
        dto.setGender(user.getGender());
        dto.setActivityLevel(user.getActivityLevel());
        dto.setGoal(user.getGoal());
        dto.setRole(user.getRole().name());

        // 🔥 Trimitem datele calculate către Frontend
        dto.setAge(age);
        dto.setBmi(bmi);
        dto.setDailyCalories(calories);

        return dto;
    }

    @Override
    @Transactional
    public UserDTO updateUserProfile(String email, UserProfileDTO dto) {
        User user = findByEmail(email);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setHeight(dto.getHeight());
        user.setWeight(dto.getWeight());

        if (dto.getBirthDate() != null) user.setBirthDate(dto.getBirthDate());
        if (dto.getGender() != null) user.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        if (dto.getActivityLevel() != null) user.setActivityLevel(ActivityLevel.valueOf(dto.getActivityLevel().toUpperCase()));
        if (dto.getGoal() != null) user.setGoal(Goal.valueOf(dto.getGoal().toUpperCase()));

        userRepository.save(user);
        return getUserDetails(email);
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        User user = findByEmail(email);
        userRepository.delete(user);
    }
}