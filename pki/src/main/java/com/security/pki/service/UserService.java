package com.security.pki.service;

import com.security.pki.dto.LoginDTO;
import com.security.pki.dto.SignUpUserDTO;
import com.security.pki.model.User;

public interface UserService {
    User register(SignUpUserDTO dto);
    User findUserById(Integer id);
    User login(String email, String password);
}
