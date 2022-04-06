package com.security.pki.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.security.pki.model.User;

@Repository
@Qualifier("userRepository")
public interface UserRepository extends JpaRepository<User , Integer> {

}
