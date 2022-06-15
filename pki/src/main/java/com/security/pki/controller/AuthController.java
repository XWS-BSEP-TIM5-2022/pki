package com.security.pki.controller;

import com.security.pki.dto.LoginDTO;
import com.security.pki.dto.SignUpUserDTO;
import com.security.pki.dto.UserTokenStateDTO;
import com.security.pki.model.Permission;
import com.security.pki.model.User;
import com.security.pki.model.UserType;
import com.security.pki.repository.VerificationTokenRepository;
import com.security.pki.security.util.TokenUtils;
import com.security.pki.service.UserService;
import com.security.pki.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;

import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private UserTypeService userTypeService;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    private static final String WHITESPACE = " ";

    static Logger log = Logger.getLogger(AuthController.class.getName());
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpUserDTO dto) throws Exception {
        try {
            User user = userService.register(dto);
            if(user == null) {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("Successful registration with email: " + user.getEmail() + ". User id: " + user.getId());

            return new ResponseEntity(user, HttpStatus.CREATED);
        } catch (Exception e) {

            if(!VALID_EMAIL_ADDRESS_REGEX.matcher( dto.getEmail()).find()){
                log.error("Registration failed for user - email invalid");

            }else{
                log.error("Registration failed for user with email: " +  dto.getEmail());
            }
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/activateAccount")
    public ResponseEntity<?> activateAccount(@RequestParam("token")String verificationToken, HttpServletRequest request) {
        if(userService.verifyUserAccount(verificationToken)) {
            String email = verificationTokenRepository.findVerificationTokenByToken(verificationToken).getUser().getEmail();
            if(!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()){
                log.error("Tried account activation with invalid token. From ip address: " + request.getRemoteAddr());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            log.info("Successfully activated account by user with email: " + email);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        log.warn("Tried account activation with invalid token: " + verificationToken + " From ip address: " + request.getRemoteAddr());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<UserTokenStateDTO> login(@RequestBody @Valid LoginDTO loginDTO, HttpServletRequest request) {
        Authentication authentication;

        if(!VALID_EMAIL_ADDRESS_REGEX.matcher(loginDTO.getEmail()).find()){
            log.error("Login failed. Email invalid. From ip address: " + request.getRemoteAddr());
            return new ResponseEntity("Email invalid", HttpStatus.BAD_REQUEST);
        }

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword()));
        } catch (Exception ex) {
            if (ex.getMessage().contains("User is disabled")) {

                log.error("Failed login. User: " + loginDTO.getEmail() + " , ip address: " + request.getRemoteAddr() + " . Account not activated.");
                return new ResponseEntity("Account is not activated", HttpStatus.BAD_REQUEST);
            }
            log.warn("Failed login. User: " + loginDTO.getEmail() + " , ip address: " + request.getRemoteAddr() + " . Bad credentials.");
            return new ResponseEntity("Bad credentials", HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        if (!user.getIsActive()) {
            log.error("Failed login. User: " + loginDTO.getEmail() + " , ip address: " + request.getRemoteAddr() + " . Account not activated.");
            return new ResponseEntity("User is not activated", HttpStatus.BAD_REQUEST);
        }

        String jwt = tokenUtils.generateToken(user.getUsername(), user.getUserType().getName(), user.getUserType().getPermissions());
        int expiresIn = tokenUtils.getExpiredIn();
        log.info("Successful login. User: " + user.getEmail() + " , ip address: " + request.getRemoteAddr());
        return ResponseEntity.ok(new UserTokenStateDTO(jwt, expiresIn));
    }

}
