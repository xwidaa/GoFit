package com.gofit.service;

import com.gofit.dto.AdminDashboardDTO;
import com.gofit.model.User;
import java.util.List;

public interface AdminService {
    AdminDashboardDTO getDashboardStats();
    List<User> getAllUsers(); // 🔥 Add this line
}