package com.security.pki.service.impl;

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
    public User registerUser(SignUpUserDTO dto) {
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
}
