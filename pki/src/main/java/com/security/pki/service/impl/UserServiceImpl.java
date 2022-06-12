package com.security.pki.service.impl;

import com.security.pki.dto.LoginDTO;
import com.security.pki.dto.SignUpUserDTO;
import com.security.pki.mapper.UserMapper;
import com.security.pki.model.User;
import com.security.pki.repository.UserRepository;
import com.security.pki.service.UserService;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User register(SignUpUserDTO dto) throws Exception {
        for(User user: userRepository.findAll()){
            if(user.getEmail().equals(dto.email)){
                throw new Exception("Email is not unique");
            }
        }
        User newUser = new UserMapper().SignUpUserDtoToUser(dto);
        newUser.setIsActive(false);

        // TODO SD: slanje emaila
        if (!checkPasswordCriteria(dto.password)) {
            String pswdError = "Password must contain minimum eight characters, at least one uppercase " +
                    "letter, one lowercase letter, one number and one special character and " +
                    "must not contain white spaces";
            System.out.println(pswdError);
            throw new Exception(pswdError);
        }
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

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean checkPasswordCriteria(String password) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 100),
                new UppercaseCharacterRule(1),
                new LowercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new SpecialCharacterRule(1),
                new WhitespaceRule()));

        RuleResult result = validator.validate(new PasswordData(password));
        return result.isValid();
    }
}
