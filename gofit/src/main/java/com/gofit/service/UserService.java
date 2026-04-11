package com.gofit.service;

import com.gofit.dto.UserDTO;
import com.gofit.model.User;

public interface UserService {

    String login(String email, String password); // 🔥 nu boolean

    User findByEmail(String email);

    void register(UserDTO dto);

    UserDTO getUserDetails(String email);
}