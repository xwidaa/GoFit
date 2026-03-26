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

        user.setActivityLevel(dto.getActivityLevel().name());
        user.setGoal(dto.getGoal().name());


        // 🔥 ADAUGĂ ASTA:
        user.setGender(dto.getGender());
        user.setRole("CLIENT");

        userRepository.save(user);
    }
}