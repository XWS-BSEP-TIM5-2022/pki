package com.security.pki.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.security.pki.dto.SignUpUserDTO;
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
		setUserType(dto.userType, user);
		setAuthorityType(dto.authorityType, user);
		List<Certificate> certificates = new ArrayList<Certificate>();
		for(CertificateDTO certDtos: dto.certificates) {
			certificates.add(new CertificateMapper().CertificateDtoToCertificate(certDtos));
		}
		user.setCertificates(certificates);
		return user;
		
	}

	private void setAuthorityType(String authorityType, User user) {
		if(authorityType.equals("ROOT")) {
			user.setAuthorityType(AuthorityType.ROOT);
		} else if(authorityType.equals("INTERMEDIATE")) {
			user.setAuthorityType(AuthorityType.INTERMEDIATE);
		} else {
			user.setAuthorityType(AuthorityType.END_ENTITY);
		}
	}

	private void setUserType(String userType, User user) {
		if(userType.equals("ADMIN")) {
			user.setUserType(UserType.ADMIN);
		} else {
			user.setUserType(UserType.USER);
		}
	}


	public User SignUpUserDtoToUser(SignUpUserDTO dto) {
		User user = new User();
		user.setEmail(dto.email);
		user.setPassword(dto.password);
		setUserType(dto.userType, user);
		setAuthorityType(dto.authorityType, user);
		user.setCertificates(new ArrayList<>());
		return user;
	}

	public UserDTO UserToUserDto(User user) {
		UserDTO dto = new UserDTO();
		dto.id = user.getId();
		// TODO Sanja: ispraviti userType i authorityType
		dto.userType = user.getUserType().toString();
		dto.authorityType = user.getAuthorityType().toString();
		dto.email = user.getEmail();
		dto.password = user.getPassword();
		if(user.getCertificates() != null) {
			ArrayList<CertificateDTO> dtos = new ArrayList<>();
			for (Certificate certificate : user.getCertificates()) {
				dtos.add(new CertificateMapper().certificateToCertificateDto(certificate));
			}
			dto.certificates = dtos;
		}
		return dto;
	}
}
