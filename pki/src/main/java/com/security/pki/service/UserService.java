package com.security.pki.service;

import com.security.pki.dto.SignUpUserDTO;
import com.security.pki.model.User;

public interface UserService {
    User registerUser(SignUpUserDTO dto);
    User findUserById(Integer id);
}