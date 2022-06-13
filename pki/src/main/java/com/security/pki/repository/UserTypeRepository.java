package com.security.pki.repository;

import com.security.pki.model.Permission;
import com.security.pki.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    UserType findByName(String roleUser);

    Permission[] findPermissionsById(Long id);
}
