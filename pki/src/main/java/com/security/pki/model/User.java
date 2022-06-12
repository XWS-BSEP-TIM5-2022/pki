package com.security.pki.model;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

	@Column(unique = false)
	private String email;
	
	private String password;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "role_id")
	private UserType userType;
	
	//private AuthorityType authorityType;		// TODO: mozda izbrisati
	
	@OneToMany(mappedBy = "user")
	@JsonBackReference
	private List<MyCertificate> certificates;

	private Boolean isActive;

}
