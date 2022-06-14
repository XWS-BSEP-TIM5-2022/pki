package com.security.pki.controller;

import com.security.pki.dto.ChangePasswordDTO;
import com.security.pki.dto.LoginDTO;
import com.security.pki.dto.SignUpUserDTO;
import com.security.pki.dto.UserDTO;
import com.security.pki.enums.UserType;
import com.security.pki.mapper.UserMapper;
import com.security.pki.model.User;
import com.security.pki.repository.UserRepository;
import com.security.pki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    static Logger log = Logger.getLogger(UserController.class.getName());


    @RequestMapping(method = RequestMethod.GET, value = "/getById/{id}")
    @PreAuthorize("hasAuthority('getUserById')")
    public ResponseEntity getUserById(@PathVariable Integer id) {
        User user = userService.findUserById(id);
        if(user == null) {
            log.error("Error while getting user with id:" + id + ". Logged user: " + SecurityContextHolder.getContext().getAuthentication().getName());
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        log.info("Successfully found user with id: " + id + ". Logged user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(new UserMapper().UserToUserDto(user));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, value = "/addAdmin")
    @PreAuthorize("hasAuthority('addAdmin')")
    public ResponseEntity<?> addAdmin(@RequestBody SignUpUserDTO dto) throws Exception {
        User user = userService.registerAdmin(dto);
        log.info("Successfully add new admin with email: " + dto.email + ". Logged user: "+ SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(user.getId());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findAll")
    @PreAuthorize("hasAuthority('findAll')")
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        log.info("Successfully found all users by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(users);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getByEmail/{email}")
    @PreAuthorize("hasAuthority('findUserByEmail')")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        log.info("Successfully found user with email: " + email);
        return ResponseEntity.ok(user);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findAllClients")
    @PreAuthorize("hasAuthority('findAllClients')")
    public ResponseEntity<List<User>> findAllClients() {
        List<User> users = new ArrayList<>();
        for (User u: userService.findAll()) {
            if(u.getUserType().equals(UserType.USER)){
                users.add(u);
            }
        }
        log.info("Successfully found all clients by user with email: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(users);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/changePassword",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('changePassword')")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto, Principal user) throws Exception {
        try {
            userService.changePassword(dto, user.getName());
            log.info("Successfully changed password by user with email: " + SecurityContextHolder.getContext().getAuthentication().getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while changing password by user with email" + SecurityContextHolder.getContext().getAuthentication().getName());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
