package com.security.pki.service;

import com.security.pki.model.UserType;

import java.util.Set;

public interface UserTypeService {
    UserType findUserTypeByName(String role_user);

    Set<String> getPermissionsById(Long id);
}
