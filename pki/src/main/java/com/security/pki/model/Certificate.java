package com.security.pki.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Certificate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	private boolean revoked; 
	
	@Column(name = "validfrom", nullable = false)
	private Date validFrom;

    @Column(name = "validto", nullable = false)
    private Date validTo;
    
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
    
    

	
	
}
