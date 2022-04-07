package com.security.pki.model;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.security.pki.enums.AuthorityType;
import com.security.pki.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(unique = true)
	private String email;
	
	private String password;
	
	private UserType userType;
	
	private AuthorityType authorityType;		// TODO: mozda izbrisati
	
	@OneToMany(mappedBy = "user")
	@JsonBackReference
	private List<Certificate> certificates;



	public User(Integer id, String email, String password, UserType userType, List<Certificate> certificates){
		this.id = id;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.certificates = certificates;
	}


}
