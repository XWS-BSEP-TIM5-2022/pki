package com.security.pki.mapper;

import com.security.pki.dto.AllCertificatesViewDTO;
import com.security.pki.dto.CreateCertificateDTO;
import com.security.pki.enums.CertificateType;
import com.security.pki.model.CertificateData;
import com.security.pki.model.CertificateUsage;
import com.security.pki.model.MyCertificate;

import com.security.pki.dto.CertificateDTO;
import com.security.pki.model.User;
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

    public MyCertificate CreateCertificateDtoToCertificate(CreateCertificateDTO dto, User user) {
		MyCertificate cert = new MyCertificate();
		cert.setValidFrom(dto.getValidFrom());
		cert.setValidTo(dto.getValidTo());
		cert.setSerialNumber(dto.getSerialNumber());
		setCertificateType(dto, cert);
		cert.setRevoked(false);
		cert.setCertificateUsage(CertificateUsage.DOCUMENT_SIGNING); // TODO: Sanja: dogovoriti se za namene sertifikata
		cert.setSerialNumber(dto.getSerialNumber());
		cert.setUser(user);
		cert.setCertificateData(new CertificateDataMapper().CertDataDtoToCertData(dto.getCertificateDataDTO()));
		return cert;
	}

	private void setCertificateType(CreateCertificateDTO dto, MyCertificate cert) {
		if(dto.getCertificateType().equals("SELF_SIGNED")){
			cert.setCertificateType(CertificateType.SELF_SIGNED);
		} else if(dto.getCertificateType().equals("INTERMEDIATE")) {
			cert.setCertificateType(CertificateType.INTERMEDIATE);
		} else {
			cert.setCertificateType(CertificateType.END_ENTITY);
		}
	}
}
