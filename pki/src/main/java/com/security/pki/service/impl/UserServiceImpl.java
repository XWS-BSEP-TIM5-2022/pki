package com.security.pki.service.impl;

import com.security.pki.controller.UserController;
import com.security.pki.dto.ChangePasswordDTO;
import com.security.pki.dto.LoginDTO;
import com.security.pki.dto.SignUpUserDTO;
import com.security.pki.mapper.UserMapper;
import com.security.pki.model.User;
import com.security.pki.model.UserType;
import com.security.pki.model.VerificationToken;
import com.security.pki.repository.UserRepository;
import com.security.pki.repository.VerificationTokenRepository;
import com.security.pki.service.EmailService;
import com.security.pki.service.UserService;
import com.security.pki.service.UserTypeService;
import com.security.pki.service.VerificationTokenService;
import org.apache.log4j.Logger;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final int TOKEN_EXPIRES_MINUTES = 15;
    private final int MIN_PASSWORD_LENGTH = 8;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    static Logger log = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public User register(SignUpUserDTO dto) throws Exception {
        for(User user: userRepository.findAll()){
            if(user.getEmail().equals(dto.email)){
                log.error("User with email: " + SecurityContextHolder.getContext().getAuthentication().getName() + "can't change password because email is not unique");
                throw new Exception("Email is not unique");
            }
        }
        if (!checkPasswordCriteria(dto.password)) {
            String pswdError = "Password must contain minimum eight characters, at least one uppercase " +
                    "letter, one lowercase letter, one number and one special character and " +
                    "must not contain white spaces";
            System.out.println(pswdError);
            log.error("User with email: " + SecurityContextHolder.getContext().getAuthentication().getName() + "can't change password because: Format of password is invalid" );

            throw new Exception(pswdError);
        }
        User newUser = new UserMapper().SignUpUserDtoToUser(dto);
        newUser.setIsActive(false);
        newUser.setLastPasswordResetDate(Timestamp.from(Instant.now()));
        UserType role = userTypeService.findUserTypeByName("ROLE_USER");
        if (role == null) {
            log.error("User with email: " + SecurityContextHolder.getContext().getAuthentication().getName() + "can't change password because: Role does not exist!" );
            throw new Exception("Role does not exist");
        }
        newUser.setUserType(role);
        newUser.setPassword(passwordEncoder.encode(dto.password));

        // TODO SD: slanje emaila
        VerificationToken verificationToken = new VerificationToken(newUser);
        if (!emailService.sendAccountActivationMail(verificationToken.getToken(), newUser.getEmail())) {
            log.error("User with email: " + SecurityContextHolder.getContext().getAuthentication().getName() + "can't change password because: Email for account verification not sent!");
            throw new Exception("Email for account verification not sent, try again");
        }
        log.info("Verification email is sent!");
        userRepository.save(newUser);
        User registeredUser = userRepository.findByEmail(newUser.getEmail());
        verificationTokenService.saveVerificationToken(verificationToken);
        return registeredUser;
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
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[-+_!@#$%^&*.,?:;<>=`~)({}|/])(?=\\S+$).{8,}$");
        Matcher passMatcher = pattern.matcher(password);
        return passMatcher.matches();
    }

    @Override
    public boolean verifyUserAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findVerificationTokenByToken(token);
        long difference_In_Time = (new Date()).getTime() - verificationToken.getCreatedDateTime().getTime();
        User user = userRepository.findByEmail(verificationToken.getUser().getEmail());
        ;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        if (difference_In_Minutes <= TOKEN_EXPIRES_MINUTES) {
            user.setIsActive(true);
            userRepository.save(user);
            return true;
        } else {
            userRepository.delete(user);
            verificationTokenRepository.delete(verificationToken);
            return false;
        }
    }

    @Override
    public User registerAdmin(SignUpUserDTO dto) throws Exception {
        for(User user: userRepository.findAll()){
            if(user.getEmail().equals(dto.email)){
                log.error("User with email: " + SecurityContextHolder.getContext().getAuthentication().getName() + "can't add new admin because: Email is not unique!");
                throw new Exception("Email is not unique");
            }
        }
        if (!checkPasswordCriteria(dto.password)) {
            String pswdError = "Password must contain minimum eight characters, at least one uppercase " +
                    "letter, one lowercase letter, one number and one special character and " +
                    "must not contain white spaces";
            log.error("User with email: " + SecurityContextHolder.getContext().getAuthentication().getName() + "can't add new admin because: Format of password is invalid" );

            System.out.println(pswdError);
            throw new Exception(pswdError);
        }
        User newUser = new UserMapper().SignUpUserDtoToUser(dto);
        newUser.setIsActive(true);
        newUser.setLastPasswordResetDate(Timestamp.from(Instant.now()));
        UserType role = userTypeService.findUserTypeByName("ROLE_ADMIN");
        if (role == null) {
            log.error("User with email: " + SecurityContextHolder.getContext().getAuthentication().getName() + "can't add new admin because: Role does not exist!");
            throw new Exception("Role does not exist");
        }
        newUser.setUserType(role);
        newUser.setPassword(passwordEncoder.encode(dto.password));
        userRepository.save(newUser);
        VerificationToken verificationToken = new VerificationToken(newUser);
        verificationTokenService.saveVerificationToken(verificationToken);
        return userRepository.findByEmail(newUser.getEmail());
    }

    @Override
    public void changePassword(ChangePasswordDTO dto, String userEmail) throws Exception {
        String pswdError = "Password must contain minimum eight characters, at least one uppercase " +
                "letter, one lowercase letter, one number and one special character and " +
                "must not contain white spaces";
        if (!checkPasswordCriteria(dto.getNewPassword())) {
            log.error("User with email: " + SecurityContextHolder.getContext().getAuthentication().getName() + "can't change password because:: Format of password is invalid" );
            throw new Exception(pswdError);
        }
        if (!checkPasswordCriteria(dto.getReenteredPassword())) {
            log.error("User with email: " + SecurityContextHolder.getContext().getAuthentication().getName() + "can't change password because:: Format of password is invalid" );
            throw new Exception(pswdError);
        }

        User user = userRepository.findByEmail(userEmail);

        if(!user.getIsActive()){
            log.error("User with email: " + SecurityContextHolder.getContext().getAuthentication().getName() + "can't change password because: Account is not activated");
            throw new Exception("Account is not activated");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            log.error("User with email: " + SecurityContextHolder.getContext().getAuthentication().getName() + "can't change password because: Old password does not match the current password");
            throw new Exception("Old password does not match the current password");
        }
        if (!dto.getNewPassword().equals(dto.getReenteredPassword())) {
            log.error("User with email: " + SecurityContextHolder.getContext().getAuthentication().getName() + "can't change password because: New passwords do not match");
            throw new Exception("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setLastPasswordResetDate(Timestamp.from(Instant.now()));
        userRepository.save(user);
    }
}
