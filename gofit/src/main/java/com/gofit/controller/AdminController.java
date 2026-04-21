package com.gofit.controller;

import com.gofit.dto.AdminDashboardDTO;
import com.gofit.dto.UserDTO;
import com.gofit.service.AdminService;
import com.gofit.model.User;  // <--- Added this
import java.util.List;        // <--- Added this
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDTO> getDashboardStats() {
        AdminDashboardDTO stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    //delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }

    //edit user
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO dto) {

        adminService.updateUser(id, dto);
        return ResponseEntity.ok("User updated");
    }

    //add user
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserDTO dto) {
        adminService.createUser(dto);
        return ResponseEntity.ok("User created");
    }



}