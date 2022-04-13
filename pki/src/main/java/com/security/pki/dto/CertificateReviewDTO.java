package com.security.pki.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.security.pki.enums.CertificateType;
import com.security.pki.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificateReviewDTO {

    private Long id;
    private String commonName;
    private String givenName;
    private String surname;
    private String organization;
    private String organizationalUnit;
    private String countryCode;
    private String emailAddress;
    private boolean revoked;
    private String validFrom;
    private String validTo;
    private String user;
    private String certificateType;
    private String serialNumber;
    private String certificateUsage;

}
