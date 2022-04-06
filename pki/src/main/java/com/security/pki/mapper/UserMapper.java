package com.security.pki.mapper;

import java.util.ArrayList;
import java.util.List;

import com.security.pki.model.AuthorityType;
import com.security.pki.model.Certificate;
import com.security.pki.model.User;
import com.security.pki.model.UserType;

import com.security.pki.dto.CertificateDTO;
import com.security.pki.dto.UserDTO;

public class UserMapper {
	
	public UserMapper() {}
	
	public User UserDtoToUser(UserDTO dto) {
		User user = new User();
		user.setEmail(dto.email);
		user.setPassword(dto.password);
		setUserType(dto, user);
		setAuthorityType(dto, user);
		List<Certificate> certificates = new ArrayList<Certificate>();
		for(CertificateDTO certDtos: dto.certificates) {
			certificates.add(new CertificateMapper().CertificateDtoToCertificate(certDtos));
		}
		user.setCertificates(certificates);
		return user;
		
	}

	private void setAuthorityType(UserDTO dto, User user) {
		if(dto.authorityType.equals("ROOT")) {
			user.setAuthorityType(AuthorityType.ROOT);
		} else if(dto.authorityType.equals("INTERMEDIATE")) {
			user.setAuthorityType(AuthorityType.INTERMEDIATE);
		} else {
			user.setAuthorityType(AuthorityType.ROOT);
		}
	}

	private void setUserType(UserDTO dto, User user) {
		if(dto.userType.equals("ADMIN")) {
			user.setUserType(UserType.ADMIN);
		} else {
			user.setUserType(UserType.USER);
		}
	}
	

}
