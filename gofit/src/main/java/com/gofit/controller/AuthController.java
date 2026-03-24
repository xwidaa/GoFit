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
            String email = userService.login(dto.getEmail(), dto.getPassword());
            String token = jwtService.generateToken(email);
            return ResponseEntity.ok(Map.of("token", token)); // {"token": "eyJ..."}
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
