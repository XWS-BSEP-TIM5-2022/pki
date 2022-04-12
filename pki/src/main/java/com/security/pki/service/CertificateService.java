package com.security.pki.service;

import com.security.pki.dto.AllCertificatesViewDTO;
import com.security.pki.dto.CreateCertificateDTO;
import com.security.pki.dto.CreateSelfSignedCertificateDTO;
import com.security.pki.enums.CertificateType;
import com.security.pki.mapper.CertificateMapper;
import com.security.pki.model.MyCertificate;
import com.security.pki.model.IssuerData;
import com.security.pki.model.SubjectData;
import com.security.pki.model.User;
import com.security.pki.repository.CertificateRepository;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.math.BigInteger;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserService userService;

    private KeyStoreWriterService ksw = new KeyStoreWriterService();
    private KeyStoreReaderService ksr = new KeyStoreReaderService();
    private CertificateMapper certificateMapper = new CertificateMapper();
    private String password = "pass";

    public List<AllCertificatesViewDTO> findAll() {
        List<AllCertificatesViewDTO> dtos = new ArrayList<>();
        for (MyCertificate c : this.certificateRepository.findAll()) {
            dtos.add(this.certificateMapper.certificateWithCommonNameToCertificateDto(c));
        }
        return dtos;
    }

    public List<MyCertificate> findAllRootsAndCA() {
        List<MyCertificate> certificates = new ArrayList<>();
        for (MyCertificate c : this.certificateRepository.findAll()) {
            if (c.getCertificateType() != CertificateType.END_ENTITY && !c.isRevoked()) {
                certificates.add(c);
            }
        }
        return certificates;
    }

    public MyCertificate findById(Integer id) {
        return this.certificateRepository.findById(id).orElseGet(null);
    }

    public List<AllCertificatesViewDTO> findAllByUser(Integer id) {
        List<AllCertificatesViewDTO> dtos = new ArrayList<>();
        for (MyCertificate c : this.certificateRepository.findAll()) {
            if (c.getUser().getId() == id) {
                dtos.add(this.certificateMapper.certificateWithCommonNameToCertificateDto(c));
            }
        }
        return dtos;
    }

    public X509Certificate issueSelfSignedCertificate(CreateSelfSignedCertificateDTO dto) {
        Security.addProvider(new BouncyCastleProvider());
        KeyPair keyPair = generateKeyPair();

        SubjectData subjectData = generateSubjectDataForSelfSigned(dto, keyPair);

        X500Name issuer = new X500NameBuilder().addRDN(BCStyle.E, dto.getIssuerName()).build();
        IssuerData issuerData = new IssuerData(issuer, keyPair.getPrivate());


        String serialNumber = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        if (!isSerialNumberUnique(serialNumber)) {
            serialNumber = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        }
        try {
            //Posto klasa za generisanje sertifikata ne moze da primi direktno privatni kljuc, pravi se builder za objekat
            //Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za potpisivanje sertifikata
            //Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje sertifiakta
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

            builder = builder.setProvider("BC");

            //Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());    // ContentSigner je wrapper oko private kljuca

            //Postavljaju se podaci za generisanje sertifikata
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
                    issuerData.getX500name(),
                    new BigInteger(serialNumber, 16),
                    dto.getValidFrom(),
                    dto.getValidTo(),
                    subjectData.getX500Name(),
                    subjectData.getPublicKey()
            );

            // TODO: sta znaci ekstenzija sertifikata - critical ???
            KeyUsage usage = new KeyUsage(KeyUsage.keyCertSign | KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.dataEncipherment | KeyUsage.cRLSign);
            certGen.addExtension(Extension.keyUsage, true, usage);

            //Generise se sertifikat
            X509CertificateHolder certHolder = certGen.build(contentSigner);    // povezujuemo sertifikat sa content signer-om (odnosno digitalnim potpisom)
            // napravi sve sa prethodno popunjenim podacima i potpisi sa privatnim kljucem onoga ko izdaje sertifikat

            //Builder generise sertifikat kao objekat klase X509CertificateHolder
            //Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            X509Certificate x509Certificate = certConverter.getCertificate(certHolder);

            writeCertificate(dto.getCertificateType(), x509Certificate, keyPair.getPrivate());

            saveIssuerPrivateKey(x509Certificate, keyPair.getPrivate());

            saveCertificateSelfSignedToDatabase(dto, serialNumber);
            //Konvertuje objekat u sertifikat (izvlacenje konkretnog sertifikata)
            return x509Certificate;
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (CertIOException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            System.out.println(" ********** cuvanje sertifikata u bazu nije uspelo **********");
            exception.printStackTrace();
        }
        return null;


    }

    public X509Certificate issueCertificate(CreateCertificateDTO dto) {
        Security.addProvider(new BouncyCastleProvider());
        KeyPair keyPairSubject = generateKeyPair();

        SubjectData subjectData = generateSubjectData(dto, keyPairSubject);

        String filename = "issuers.jks";
        PrivateKey privateKeyIssuer = findPrivateKeyFromKeyStore(getPath(filename), new BigInteger(dto.getIssuerSerialNumber(), 16).toString());  // TODO: mozda ne radi
        X500Name issuer = new X500NameBuilder().addRDN(BCStyle.E, dto.getIssuerName()).build();
        IssuerData issuerData = new IssuerData(issuer, privateKeyIssuer);

        String serialNumber = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        if (!isSerialNumberUnique(serialNumber)) {
            serialNumber = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        }

        try {
            //Posto klasa za generisanje sertifikata ne moze da primi direktno privatni kljuc, pravi se builder za objekat
            //Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za potpisivanje sertifikata
            //Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje sertifiakta
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

            builder = builder.setProvider("BC");

            //Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());    // ContentSigner je wrapper oko private kljuca

            //Postavljaju se podaci za generisanje sertifikata
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
                    issuerData.getX500name(),
                    new BigInteger(serialNumber, 16),
                    dto.getValidFrom(),
                    dto.getValidTo(),
                    subjectData.getX500Name(),
                    subjectData.getPublicKey()
            );

            // TODO: sta znaci ekstenzija sertifikata - critical ???
            KeyUsage usage = new KeyUsage(KeyUsage.keyCertSign | KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.dataEncipherment | KeyUsage.cRLSign);
            certGen.addExtension(Extension.keyUsage, true, usage);

            //Generise se sertifikat
            X509CertificateHolder certHolder = certGen.build(contentSigner);    // povezujuemo sertifikat sa content signer-om (odnosno digitalnim potpisom)
            // napravi sve sa prethodno popunjenim podacima i potpisi sa privatnim kljucem onoga ko izdaje sertifikat

            //Builder generise sertifikat kao objekat klase X509CertificateHolder
            //Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            X509Certificate x509Certificate = certConverter.getCertificate(certHolder);

            writeCertificate(dto.getCertificateType(), x509Certificate, keyPairSubject.getPrivate());

            if(findPrivateKeyFromKeyStore(getPath("issuers.jks"), new BigInteger(dto.getIssuerSerialNumber(), 16).toString()) == null){
                System.out.println("********************************JESTE******************");
                saveIssuerPrivateKey(x509Certificate, privateKeyIssuer);
            }

            saveCertificateToDatabase(dto, serialNumber);
            //Konvertuje objekat u sertifikat (izvlacenje konkretnog sertifikata)
            return x509Certificate;

        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (CertIOException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            System.out.println(" ********** cuvanje sertifikata u bazu nije uspelo **********");
            exception.printStackTrace();
        }
        return null;
    }

    private void saveCertificateSelfSignedToDatabase(CreateSelfSignedCertificateDTO dto, String serialNumber) throws Exception {
        User user = userService.findByEmail(dto.getSubjectName());
        if (user == null) {
            throw new Exception();
        }
        MyCertificate myCertificate = new CertificateMapper().CreateCertificateDtoToCertificate(dto, user);
        myCertificate.setSerialNumber(serialNumber);
        certificateRepository.save(myCertificate);
    }

    private void saveCertificateToDatabase(CreateCertificateDTO dto, String serialNumber) throws Exception {
        User user = userService.findByEmail(dto.getSubjectName());
        if (user == null) {
            throw new Exception();
        }
        MyCertificate myCertificate = new CertificateMapper().CreateCertificateDtoToCertificate(dto, user);
        myCertificate.setSerialNumber(serialNumber);
        certificateRepository.save(myCertificate);
    }

    private void writeCertificate(String certificateType, X509Certificate x509Certificate, PrivateKey privateKey) {
        ksw.loadKeyStore(null, password.toCharArray());

        ksw.write(x509Certificate.getSerialNumber().toString(), privateKey, password.toCharArray(), x509Certificate);

        if (certificateType.equals(CertificateType.END_ENTITY.toString())) {
            ksw.saveKeyStore(getPath("ee.jks"), password.toCharArray());
            readCertificate(x509Certificate, "ee.jks");
        } else if (certificateType.equals(CertificateType.INTERMEDIATE.toString())) {
            ksw.saveKeyStore(getPath("ca.jks"), password.toCharArray());
            readCertificate(x509Certificate, "ca.jks");
        } else if (certificateType.equals(CertificateType.SELF_SIGNED.toString())) {
            ksw.saveKeyStore(getPath("root.jks"), password.toCharArray());
            readCertificate(x509Certificate, "root.jks");
        }
    }

    private void saveIssuerPrivateKey(X509Certificate x509Certificate, PrivateKey privateKeyIssuer) {

        ksw.loadKeyStore(null, password.toCharArray());
        ksw.write(x509Certificate.getSerialNumber().toString(), privateKeyIssuer, password.toCharArray(), x509Certificate);

        ksw.saveKeyStore(getPath("issuers.jks"), password.toCharArray());

        readCertificate(x509Certificate, "issuers.jks");
    }

    private void readCertificate(X509Certificate x509Certificate, String path) {
        java.security.cert.Certificate c = ksr.readCertificate(getPath(path), password, x509Certificate.getSerialNumber().toString());
        System.out.println("----------------------------------UCITAN--------------------------------------");
        System.out.println(c);
        System.out.println("----------------------------------KRAJ----------------------------------------");
    }

    private String getPath(String path) {
        return Paths.get(FileSystems.getDefault().getPath("").toAbsolutePath().toString(), "src", "main", "resources", "keystores", path).toString();
    }

    private SubjectData generateSubjectDataForSelfSigned(CreateSelfSignedCertificateDTO dto, KeyPair keyPairSubject) {
        //klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
        // (konkretno, objekat se napravi nakon poziva metode build nad objektom tipa X500NameBuilder)
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, dto.getCertificateDataDTO().getCommonName());
        builder.addRDN(BCStyle.SURNAME, dto.getCertificateDataDTO().getSurname());
        builder.addRDN(BCStyle.GIVENNAME, dto.getCertificateDataDTO().getGivenName());
        builder.addRDN(BCStyle.O, dto.getCertificateDataDTO().getOrganization());
        builder.addRDN(BCStyle.OU, dto.getCertificateDataDTO().getOrganizationalUnit());
        builder.addRDN(BCStyle.POSTAL_CODE, dto.getCertificateDataDTO().getCountryCode());
        builder.addRDN(BCStyle.E, dto.getCertificateDataDTO().getEmailAddress());

        //UID (USER ID) je ID korisnika
        String id = dto.getCertificateDataDTO().getUserId().toString();
        builder.addRDN(BCStyle.UID, id);

        return new SubjectData(keyPairSubject.getPublic(), builder.build());
    }


    private SubjectData generateSubjectData(CreateCertificateDTO dto, KeyPair keyPairSubject) {
        //klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
        // (konkretno, objekat se napravi nakon poziva metode build nad objektom tipa X500NameBuilder)
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, dto.getCertificateDataDTO().getCommonName());
        builder.addRDN(BCStyle.SURNAME, dto.getCertificateDataDTO().getSurname());
        builder.addRDN(BCStyle.GIVENNAME, dto.getCertificateDataDTO().getGivenName());
        builder.addRDN(BCStyle.O, dto.getCertificateDataDTO().getOrganization());
        builder.addRDN(BCStyle.OU, dto.getCertificateDataDTO().getOrganizationalUnit());
        builder.addRDN(BCStyle.POSTAL_CODE, dto.getCertificateDataDTO().getCountryCode());
        builder.addRDN(BCStyle.E, dto.getCertificateDataDTO().getEmailAddress());

        String id = dto.getCertificateDataDTO().getUserId().toString();
        builder.addRDN(BCStyle.UID, id);

        return new SubjectData(keyPairSubject.getPublic(), builder.build());
    }
    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

            // RSA algoritam trazi neki seed, neku pocetnu vrednost od koje krece (s kojom vrsi XOR veliki broj puta)
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");      // TODO: proveriti
            keyGen.initialize(2048, random);        // inicijalizacija generatora, duzina kljuca je 2048

            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isSerialNumberUnique(String serialNumber) {

        for (MyCertificate certificate : certificateRepository.findAll()) {
            if (certificate.getSerialNumber().equals(serialNumber)) {
                return false;
            }
        }

        return true;
    }

    //findPrivateKeyFromKeyStore(getPath("issuers.jks"), x509Certificate.getSerialNumber().toString()); OVAKO POZIV!!!!!!!!!!!!!
    private PrivateKey findPrivateKeyFromKeyStore(String fileName, String serialNumber) { //TODO: promeniti da se salje pass usera???

        ksw.loadKeyStore(fileName, password.toCharArray());
        PrivateKey pk = ksr.readPrivateKey(fileName, password, serialNumber, password);
        return pk;
    }

}
