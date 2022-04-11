package com.security.pki.dto;

import com.security.pki.enums.CertificateType;
import com.security.pki.model.CertificateUsage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateCertificateDTO {
    private Date validFrom;
    private Date validTo;
    private String issuerName;
    private String subjectName;
    private CertificateDataDTO certificateDataDTO;
    private String certificateType;
    private String certificateUsage;
}
