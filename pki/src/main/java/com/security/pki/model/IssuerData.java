package com.security.pki.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.security.PrivateKey;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class IssuerData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //private X500Name x500name;
    private PrivateKey privateKey;

//    private String commonName;
//    private String surname;
//    private String givenName;
//    private String organisation;
//    private String organisationUnit;
//    private String country;
//    private String email;

//    @OneToMany(mappedBy = "issuerData")
//    private List<Certificate> certificate;
}
