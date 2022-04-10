package com.security.pki.mapper;

import com.security.pki.dto.AllCertificatesViewDTO;
import com.security.pki.model.MyCertificate;

import com.security.pki.dto.CertificateDTO;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
public class CertificateMapper {
	
	
	public MyCertificate CertificateDtoToCertificate(CertificateDTO dto) {
		MyCertificate cert = new MyCertificate();
		cert.setRevoked(dto.revoked);
		cert.setValidFrom(dto.validFrom);
		cert.setValidTo(dto.validTo);
		return cert;
	}

	public CertificateDTO certificateToCertificateDto(MyCertificate certificate) {
		CertificateDTO dto = new CertificateDTO();
		dto.revoked = certificate.isRevoked();
		dto.validFrom = certificate.getValidFrom();
		dto.validTo = certificate.getValidTo();
		dto.id = certificate.getId();
		return dto;
	}

	public AllCertificatesViewDTO certificateWithCommonNameToCertificateDto(MyCertificate certificate) {
		AllCertificatesViewDTO dto = new AllCertificatesViewDTO();
		dto.id = certificate.getId();
		//dto.commonName = certificate.getSubjectData().getCommonName();
		dto.validFrom = convertDateToString(certificate.getValidFrom());
		dto.validTo = convertDateToString(certificate.getValidTo());
		dto.email = certificate.getUser().getEmail();
		dto.certificateType = certificate.getCertificateType().toString();

		return dto;
	}

	public String convertDateToString(Date dt) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dateToString = df.format(dt);
		return dateToString;
	}
}
