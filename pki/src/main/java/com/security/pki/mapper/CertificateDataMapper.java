package com.security.pki.mapper;

import com.security.pki.dto.CertificateDataDTO;
import com.security.pki.model.CertificateData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CertificateDataMapper {

    public CertificateData CertDataDtoToCertData(CertificateDataDTO dto) {
        CertificateData certificateData = new CertificateData();
        certificateData.setCommonName(dto.getCommonName());
        certificateData.setCountryCode(dto.getCountryCode());
        certificateData.setGivenName(dto.getGivenName());
        certificateData.setOrganization(dto.getOrganization());
        certificateData.setEmailAddress(dto.getEmailAddress());
        certificateData.setOrganizationalUnit(dto.getOrganizationalUnit());
        certificateData.setSurname(dto.getSurname());
        return certificateData;
    }
}
