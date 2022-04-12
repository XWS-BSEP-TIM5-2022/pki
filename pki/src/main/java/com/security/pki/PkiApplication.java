package com.security.pki;


import com.security.pki.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.security.pki.model.User;
import com.security.pki.enums.UserType;

import com.security.pki.repository.UserRepository;

import java.util.Calendar;


@SpringBootApplication
public class PkiApplication implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CertificateRepository certificateRepository;


	public static void main(String[] args)  {
		SpringApplication.run(PkiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		User admin = new User(1, "admin@gmail.com", "admin", UserType.ADMIN, null);
//		userRepository.save(admin);
//
//		User user = new User(2, "user@gmail.com", "user", UserType.USER, null);
//		userRepository.save(user);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2022);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);


//		SubjectData subjectData = new SubjectData();
//		subjectData.setCommonName("Ceritificate 1");
//		subjectDataRepository.save(subjectData);
//		SubjectData subjectData2 = new SubjectData();
//		subjectData2.setCommonName("Ceritificate 2");
//		subjectDataRepository.save(subjectData2);
//
//		Certificate certificate = new Certificate(1, false, cal.getTime(), cal.getTime(), admin);
//		certificate.setSubjectData(subjectData);
//		certificate.setCertificateType(CertificateType.SELF_SIGNED);
//		certificateRepository.save(certificate);
//
//		Certificate certificate2 = new Certificate(2, false, cal.getTime(), cal.getTime(), user);
//		certificate2.setSubjectData(subjectData2);
//		certificate2.setCertificateType(CertificateType.INTERMEDIATE);
//		certificateRepository.save(certificate2);
	}

}
