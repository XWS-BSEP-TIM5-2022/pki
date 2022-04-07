package com.security.pki.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.security.PublicKey;
import java.util.List;
//import org.bouncycastle.asn1.x500.X500Name;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SubjectData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private PublicKey publicKey;
//    private X500Name x500name;

    private String commonName;
    private String surname;
    private String givenName;
    private String organisation;
    private String organisationUnit;
    private String country;
    private String email;

    @OneToMany(mappedBy = "subjectData")
    private List<Certificate> certificate;
}
