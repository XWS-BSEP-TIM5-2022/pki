package com.security.pki.dto;

import lombok.Getter;

@Getter
public class SignUpUserDTO {
    public String email;

    public String password;

    public String userType;

    public String authorityType;
}
