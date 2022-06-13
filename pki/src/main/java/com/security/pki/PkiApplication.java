package com.security.pki;


import com.security.pki.model.Permission;
import com.security.pki.model.UserType;
import com.security.pki.repository.CertificateRepository;
import com.security.pki.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.security.pki.model.User;
import com.security.pki.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
public class PkiApplication implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CertificateRepository certificateRepository;

	@Autowired
	private UserTypeRepository userTypeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args)  {
		SpringApplication.run(PkiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/* ADMIN */
		Set<Permission> adminPermissions = getAdminPermissions();
		/* USER */
		Set<Permission> userPermissions = getUserPermissions();

		UserType userRole = new UserType();
		userRole.setName("ROLE_USER");
		userRole.setPermissions(userPermissions);
		userTypeRepository.save(userRole);

		UserType adminRole = new UserType();
		adminRole.setName("ROLE_ADMIN");
		adminRole.setPermissions(adminPermissions);
		userTypeRepository.save(adminRole);

		User admin = new User(1, "admin@gmail.com", passwordEncoder.encode("admin"), adminRole, null, true, Timestamp.from(Instant.now()));
		userRepository.save(admin);

		User user = new User(2, "user@gmail.com", passwordEncoder.encode("user"), userRole, null, true, Timestamp.from(Instant.now()));
		userRepository.save(user);
	}

	private Set<Permission> getUserPermissions() {
		Set<Permission> userPermissions = new HashSet<>();
		Permission changePassword1 = new Permission("changePassword");
		userPermissions.add(changePassword1);

		Permission getAllCertificatesByUser = new Permission("getAllCertificatesByUser");
		userPermissions.add(getAllCertificatesByUser);

		Permission downloadCertificate = new Permission("downloadCertificate");
		userPermissions.add(downloadCertificate);

		Permission findCertificateById = new Permission("findCertificateById");
		userPermissions.add(findCertificateById);

		Permission findAllRootsAndCA = new Permission("findAllRootsAndCA");
		userPermissions.add(findAllRootsAndCA);

		Permission findUserByCertificateSerialNumber = new Permission("findUserByCertificateSerialNumber");
		userPermissions.add(findUserByCertificateSerialNumber);

		Permission findCertificateBySerialNumber = new Permission("findCertificateBySerialNumber");
		userPermissions.add(findCertificateBySerialNumber);

		Permission findAllRootAndCAByUser = new Permission("findAllRootAndCAByUser");
		userPermissions.add(findAllRootAndCAByUser);

		Permission findIssuerEmailBySerialNumber = new Permission("findIssuerEmailBySerialNumber");
		userPermissions.add(findIssuerEmailBySerialNumber);

		Permission findBySerialNumber = new Permission("findBySerialNumber");
		userPermissions.add(findBySerialNumber);

		Permission getUserById = new Permission("getUserById");
		userPermissions.add(getUserById);

		Permission findUserByEmail = new Permission("findUserByEmail");
		userPermissions.add(findUserByEmail);
		return userPermissions;
	}

	private Set<Permission> getAdminPermissions() {
		Set<Permission> adminPermissions = new HashSet<>();
		Permission changePassword = new Permission("changePassword");
		adminPermissions.add(changePassword);

		Permission getAllCertificates = new Permission("getAllCertificates");
		adminPermissions.add(getAllCertificates);

		Permission getAllCertificatesByUser1 = new Permission("getAllCertificatesByUser");
		adminPermissions.add(getAllCertificatesByUser1);

		Permission downloadCertificate1 = new Permission("downloadCertificate");
		adminPermissions.add(downloadCertificate1);

		Permission findCertificateById1 = new Permission("findCertificateById");
		adminPermissions.add(findCertificateById1);

		Permission createCertificate = new Permission("createCertificate");
		adminPermissions.add(createCertificate);

		Permission createSelfSigned = new Permission("createSelfSigned");
		adminPermissions.add(createSelfSigned);

		Permission findAllRootsAndCA1 = new Permission("findAllRootsAndCA");
		adminPermissions.add(findAllRootsAndCA1);

		Permission findUserByCertificateSerialNumber1 = new Permission("findUserByCertificateSerialNumber");
		adminPermissions.add(findUserByCertificateSerialNumber1);

		Permission revokeCertificate = new Permission("revokeCertificate");
		adminPermissions.add(revokeCertificate);

		Permission findCertificateBySerialNumber1 = new Permission("findCertificateBySerialNumber");
		adminPermissions.add(findCertificateBySerialNumber1);

		Permission findAllRootAndCAByUser1 = new Permission("findAllRootAndCAByUser");
		adminPermissions.add(findAllRootAndCAByUser1);

		Permission findIssuerEmailBySerialNumber1 = new Permission("findIssuerEmailBySerialNumber");
		adminPermissions.add(findIssuerEmailBySerialNumber1);

		Permission findBySerialNumber1 = new Permission("findBySerialNumber");
		adminPermissions.add(findBySerialNumber1);

		Permission getUserById1 = new Permission("getUserById");
		adminPermissions.add(getUserById1);

		Permission addAdmin = new Permission("addAdmin");
		adminPermissions.add(addAdmin);

		Permission findAll = new Permission("findAll");
		adminPermissions.add(findAll);

		Permission findUserByEmail1 = new Permission("findUserByEmail");
		adminPermissions.add(findUserByEmail1);

		Permission findAllClients = new Permission("findAllClients");
		adminPermissions.add(findAllClients);
		return adminPermissions;
	}
}
