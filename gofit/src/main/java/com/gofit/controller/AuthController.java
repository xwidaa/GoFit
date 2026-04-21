package com.gofit.controller;

import com.gofit.dto.UserDTO;
import com.gofit.dto.LoginDTO;
import com.gofit.service.UserService;
import com.gofit.security.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO dto) {
        try {
            userService.register(dto);
            return ResponseEntity.ok("Registered");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {
            // 1. Validate credentials and get the email
            String email = userService.login(dto.getEmail(), dto.getPassword());

            // 2. 🔥 FETCH the user from the database to get their role
            com.gofit.model.User user = userService.findByEmail(email);

            // 3. 🔥 PASS both email AND role to generateToken
            // This satisfies the "Expected 2 arguments" requirement
            String token = jwtService.generateToken(email, user.getRole().name());

            return ResponseEntity.ok(Map.of("token", token,"role", user.getRole().name()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
