package com.security.pki.controller;

import com.security.pki.dto.AllCertificatesViewDTO;
import com.security.pki.dto.CreateCertificateDTO;
import com.security.pki.dto.CreateSelfSignedCertificateDTO;
import com.security.pki.model.MyCertificate;
import com.security.pki.model.User;
import com.security.pki.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.cert.X509Certificate;
import java.util.List;

@RestController
@RequestMapping(value = "api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @RequestMapping(value="", method = RequestMethod.GET)
    public List<AllCertificatesViewDTO> getAll() {
        return this.certificateService.findAll();
    }

    @RequestMapping(value="/getAllByUser/{id}", method = RequestMethod.GET)
    public List<AllCertificatesViewDTO> getAllByUser(@PathVariable Integer id) {
        return this.certificateService.findAllByUser(id);
    }

    @RequestMapping(value="/downloadCertificate/{id}", method = RequestMethod.GET)
    public void downloadCertificate(@PathVariable Integer id) {
//        return this.certificateService.findAllByUser(id);
        System.out.println("TODO DOWNLOAD SERTIFIKATA");
    }


    @RequestMapping(value="/findById/{id}", method = RequestMethod.GET)
    public MyCertificate findById(@PathVariable Integer id) {
        return this.certificateService.findById(id);
    }

    @RequestMapping(value="/create", method = RequestMethod.POST)
    public ResponseEntity<?> issueCertificate(@RequestBody CreateCertificateDTO dto) {
        X509Certificate certificate = certificateService.issueCertificate(dto);
//        System.out.println("-------------------------------------------------------");
//        System.out.println(certificate.getKeyUsage());
//        System.out.println("-------------------------------------------------------");

        if(certificate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

//        System.out.println("-------------------------------------------------------");
//        System.out.println(certificate);
//        System.out.println("-------------------------------------------------------");

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @RequestMapping(value="/createSelfSigned", method = RequestMethod.POST)
    public ResponseEntity<?> createSelfSigned(@RequestBody CreateSelfSignedCertificateDTO dto) {
        X509Certificate certificate = certificateService.issueSelfSignedCertificate(dto);
//        System.out.println("-------------------------------------------------------");
//        System.out.println(certificate.getKeyUsage());
//        System.out.println("-------------------------------------------------------");
        if(certificate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
//        System.out.println("-------------------------------------------------------");
//        System.out.println(certificate);
//        System.out.println("-------------------------------------------------------");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value="/findAllRootsAndCA", method = RequestMethod.GET)
    public List<MyCertificate> findAllRootsAndCA() {
        return this.certificateService.findAllRootsAndCA();
    }

    @RequestMapping(value="/findUserByCertificateSerialNumber/{serialNumber}", method = RequestMethod.GET)
    public User findUserByCertificateSerialNumber(@PathVariable String serialNumber) {
        MyCertificate certificate = this.certificateService.findMyCertificateBySerialNumber(serialNumber);
        return certificate.getUser();
    }
}
