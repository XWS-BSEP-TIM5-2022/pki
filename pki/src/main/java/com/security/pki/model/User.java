package com.security.pki.model;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
	
	private String email;
	
	private String password;
	
	private UserType userType;
	
	private AuthorityType authorityType;
	
	@OneToMany(mappedBy = "user")
	@JsonBackReference
	private List<Certificate> certificates;
	
	
	
	
	

}
