package com.security.pki.controller;

import com.security.pki.dto.SignUpUserDTO;
import com.security.pki.dto.UserDTO;
import com.security.pki.mapper.UserMapper;
import com.security.pki.model.User;
import com.security.pki.repository.UserRepository;
import com.security.pki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, value = "/signup")
    public ResponseEntity<?> user(@RequestBody SignUpUserDTO dto) {
        User user = userService.registerUser(dto);
        return ResponseEntity.ok(user.getId());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getById/{id}")
    public ResponseEntity getUserById(@PathVariable Integer id) {
        User user = userService.findUserById(id);
        if(user == null) {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new UserMapper().UserToUserDto(user));
    }
}
