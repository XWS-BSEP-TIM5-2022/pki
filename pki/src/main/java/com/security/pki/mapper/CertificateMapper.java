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
		cert.setId(dto.getId());
		cert.setRevoked(dto.getRevoked());
		cert.setValidFrom(dto.getValidFrom());
		cert.setValidTo(dto.getValidTo());

		return cert;
	}

	public CertificateDTO certificateToCertificateDto(MyCertificate certificate) {
		CertificateDTO dto = new CertificateDTO();
		dto.setRevoked(certificate.isRevoked());
		dto.setValidFrom(certificate.getValidFrom());
		dto.setValidTo(certificate.getValidTo());
		dto.setId(certificate.getId());
		CertificateDataMapper cdMapper = new CertificateDataMapper();
		dto.setCertificateDataDTO(cdMapper.certDataToCertDTO(certificate.getCertificateData()));
		dto.setCertificateType(certificate.getCertificateType().toString());
		dto.setCertificateUsage(CertificateUsage.DOCUMENT_SIGNING.toString());
		return dto;
	}

	public AllCertificatesViewDTO certificateWithCommonNameToCertificateDto(MyCertificate certificate) {
		AllCertificatesViewDTO dto = new AllCertificatesViewDTO();
		dto.id = certificate.getId();
		dto.commonName = certificate.getCertificateData().getCommonName();
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
		setCertificateType(dto, cert);
		cert.setRevoked(false);
		cert.setCertificateUsage(CertificateUsage.DOCUMENT_SIGNING); // TODO: Sanja: dogovoriti se za namene sertifikata
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
