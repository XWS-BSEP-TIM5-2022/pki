package com.security.pki.dto;

import java.util.List;

public class UserDTO {
	
	public String email;
	
	public String password;
	
	public String userType;
	
	public String authorityType;
	
	public List<CertificateDTO> certificates;
}
