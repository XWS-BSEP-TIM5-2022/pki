package com.security.pki.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.security.pki.model.User;

public interface UserRepository extends JpaRepository<User , Integer> {
    public User findByEmail(String email);
}
