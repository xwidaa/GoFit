package com.gofit.controller;

import com.gofit.dto.UserDTO;
import com.gofit.dto.UserProfileDTO;
import com.gofit.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user") // Am păstrat "/user" conform codului tău
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. GET PROFILE (Înlocuiește vechiul /me cu o logică mai curată via Service)
    @GetMapping("/me")
    public ResponseEntity<?> getMe(Principal principal) {
        try {
            // Service-ul se va ocupa de calculul BMI, vârstei și caloriilor
            return ResponseEntity.ok(userService.getUserDetails(principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());
        }
    }

    // 2. UPDATE PROFILE
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileDTO profileDTO, Principal principal) {
        try {
            UserDTO updatedUser = userService.updateUserProfile(principal.getName(), profileDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating profile: " + e.getMessage());
        }
    }

    // 3. DELETE PROFILE
    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteProfile(Principal principal) {
        try {
            userService.deleteUser(principal.getName());
            return ResponseEntity.ok().body("{\"message\": \"Account deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting account: " + e.getMessage());
        }
    }
}