package com.gofit.service;

import com.gofit.dto.AdminDashboardDTO;
import com.gofit.dto.UserDTO;
import com.gofit.model.User;
import java.util.List;

public interface AdminService {

    AdminDashboardDTO getDashboardStats();

    List<User> getAllUsers();

    void deleteUser(Long id); // 🔥 DOAR declarație (fără {})

    void updateUser(Long id, UserDTO dto);

    void createUser(UserDTO dto);
}