package mapper;

import com.security.pki.model.Certificate;

import dto.CertificateDTO;
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

}
