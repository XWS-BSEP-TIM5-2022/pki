package com.security.pki.model;

import java.util.Date;
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
public class Certificate {

	//TODO: svuda staviti u polja nullable = false

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	private boolean revoked; 
	
	@Column(name = "validfrom", nullable = false)
	private Date validFrom;

    @Column(name = "validto", nullable = false)
    private Date validTo;
    
    @ManyToOne()
    @JoinColumn(name = "user")
	@JsonManagedReference
	private User user;		// subject info - kome se sertifikat izdaje
							// TODO: mozda user da bude u SubjectData ?
	@ManyToOne
	@JoinColumn(name="subject")
	private SubjectData subjectData;

//	@ManyToOne
//	@JoinColumn(name="issuer")
//	private IssuerData issuerData;

	@Column(name = "certificateType")
	private CertificateType certificateType;

	@Column(name = "serialNumber")
	private String serialNumber;	// potrebno kod OCSP protokola za proveru povucenosti sertifikata

	@Column(name = "certificateUsage")
	private String certificateUsage; 	// namena sertifikata

	public Certificate(Integer id, boolean revoked, Date validFrom, Date validTo, User user){
		this.id = id;
		this.revoked = revoked;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.user = user;
	}

}
