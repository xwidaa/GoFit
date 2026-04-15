package com.gofit.service;

import com.gofit.dto.UserDTO;
import com.gofit.dto.UserProfileDTO; // Asigură-te că ai acest DTO creat
import com.gofit.model.User;

public interface UserService {

    String login(String email, String password);

    User findByEmail(String email);

    void register(UserDTO dto);

    UserDTO getUserDetails(String email);

    // 🔥 Metodele noi:
    UserDTO updateUserProfile(String email, UserProfileDTO profileDTO);

    void deleteUser(String email);
}