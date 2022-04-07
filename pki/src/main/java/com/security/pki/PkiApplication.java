package com.security.pki;


import com.security.pki.model.Certificate;
import com.security.pki.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.security.pki.model.AuthorityType;
import com.security.pki.model.User;
import com.security.pki.model.UserType;

import com.security.pki.repository.UserRepository;

import java.sql.Date;
import java.time.LocalDateTime;
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
//		User admin = new User();
//		admin.setId(1);
//		admin.setEmail("admin@gmail.com");
//		admin.setPassword("admin");
//		admin.setAuthorityType(AuthorityType.ROOT);
//		admin.setUserType(UserType.ADMIN);
//		admin.setCertificates(new ArrayList<>());
//		
//		userRepository.save(admin);
		User admin = new User(1, "admin@gmail.com", "admin", UserType.ADMIN, AuthorityType.ROOT, null);
		userRepository.save(admin);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2022);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		Certificate certificate = new Certificate(1, false, cal.getTime(), cal.getTime(), admin);
		certificateRepository.save(certificate);
	}

}
