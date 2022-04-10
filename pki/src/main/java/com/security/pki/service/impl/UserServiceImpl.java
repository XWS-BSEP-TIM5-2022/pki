package com.security.pki.service.impl;

import com.security.pki.dto.LoginDTO;
import com.security.pki.dto.SignUpUserDTO;
import com.security.pki.mapper.UserMapper;
import com.security.pki.model.User;
import com.security.pki.repository.UserRepository;
import com.security.pki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User register(SignUpUserDTO dto) {
        for(User user: userRepository.findAll()){
            if(user.getEmail().equals(dto.email)){
                return null;
            }
        }
        User newUser = new UserMapper().SignUpUserDtoToUser(dto);
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public User findUserById(Integer id) {
        for(User user: userRepository.findAll()){
            if(user.getId() == id){
                return user;
            }
        }
        return null;
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user != null) {
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String subjectName) {
        return userRepository.findByEmail(subjectName);
    }

}
