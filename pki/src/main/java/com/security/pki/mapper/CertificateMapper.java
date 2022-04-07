package com.security.pki.mapper;

import com.security.pki.model.Certificate;

import com.security.pki.dto.CertificateDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CertificateMapper {
	
	
	public Certificate CertificateDtoToCertificate(CertificateDTO dto) {
		Certificate cert = new Certificate();
		cert.setRevoked(dto.revoked);
		cert.setValidFrom(dto.validFrom);
		cert.setValidTo(dto.validTo);
		return cert;
	}

	public CertificateDTO certificateToCertificateDto(Certificate certificate) {
		CertificateDTO dto = new CertificateDTO();
		dto.revoked = certificate.isRevoked();
		dto.validFrom = certificate.getValidFrom();
		dto.validTo = certificate.getValidTo();
		dto.id = certificate.getId();
		return dto;
	}
}
