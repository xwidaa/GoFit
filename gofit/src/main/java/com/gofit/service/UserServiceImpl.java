package com.gofit.service;

import com.gofit.dto.UserDTO;
import com.gofit.model.User;
import com.gofit.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    // 🔥 LOGIN -> returnează email pentru JWT
    @Override
    public String login(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return user.getEmail(); // 🔥 folosit pentru JWT
    }



    @Override
    public void register(UserDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setHeight(dto.getHeight());
        user.setWeight(dto.getWeight());
        user.setBirthDate(dto.getBirthDate());

        // 🔥 FIX 1 & 2: Remove .name() - pass the Enum objects directly
        user.setActivityLevel(dto.getActivityLevel());
        user.setGoal(dto.getGoal());
        user.setGender(dto.getGender());

        // 🔥 FIX 3: Use the Role Enum instead of "CLIENT"
        // (Assuming you named the enum constant USER or CLIENT in your Role.java)
        user.setRole(com.gofit.model.Role.USER);

        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDTO getUserDetails(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

        // 🔥 LINIA CRITICĂ: Trimitem rolul către Frontend
        // Folosim .name() pentru a transforma Enum-ul ROLE în String ("ADMIN" sau "USER")
        dto.setRole(user.getRole().name());

        return dto;
    }
}