package com.security.pki.controller;

import com.security.pki.dto.*;
import com.security.pki.mapper.CertificateMapper;
import com.security.pki.model.MyCertificate;
import com.security.pki.model.User;
import com.security.pki.service.CertificateService;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;
//    @Autowired
//    private Base64Encoder base64Encoder;

    @RequestMapping(value="", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('getAllCertificates')")
    public List<AllCertificatesViewDTO> getAll() {
        return this.certificateService.findAll();
    }

    @RequestMapping(value="/getAllByUser/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('getAllCertificatesByUser')")
    public List<AllCertificatesViewDTO> getAllByUser(@PathVariable Integer id) {
        return this.certificateService.findAllByUser(id);
    }

    @RequestMapping(value="/downloadCertificate/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('downloadCertificate')")
    public ResponseEntity<?> downloadCertificate(@PathVariable Integer id) throws KeyStoreException, CertificateEncodingException, IOException {
        MyCertificate cert = certificateService.findById(id);
        if(cert.isRevoked()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Certificate certificate = certificateService.findCertificateBySerialNumber(cert.getSerialNumber(), cert.getCertificateType().toString());
        certificateService.downloadCert(certificate, cert.getSerialNumber());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/findById/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('findCertificateById')")
    public CertificateReviewDTO findById(@PathVariable Integer id) {
        return this.certificateService.findDtoById(id);
    }

    @RequestMapping(value="/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('createCertificate')")
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
    @PreAuthorize("hasAuthority('createSelfSigned')")
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
    @PreAuthorize("hasAuthority('findAllRootsAndCA')")
    public List<MyCertificate> findAllRootsAndCA() {
        return this.certificateService.findAllRootsAndCA();
    }

    @RequestMapping(value="/findUserByCertificateSerialNumber/{serialNumber}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('findUserByCertificateSerialNumber')")
    public User findUserByCertificateSerialNumber(@PathVariable String serialNumber) {
        MyCertificate certificate = this.certificateService.findMyCertificateBySerialNumber(serialNumber);
        return certificate.getUser();
    }


    @RequestMapping(value="/revokeCerificate/{serialNumber}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('revokeCertificate')")
    public void revokeCerificate(@PathVariable String serialNumber){
        System.out.println("EVO ME OVDE:" + serialNumber);
        certificateService.revokeCerificate(serialNumber);
    }
  
    @RequestMapping(value="/findCertificateBySerialNumber/{serialNumber}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('findCertificateBySerialNumber')")
    public MyCertificate findCertificateBySerialNumber(@PathVariable String serialNumber) {
        return this.certificateService.findMyCertificateBySerialNumber(serialNumber);
    }

    @RequestMapping(value="/findAllRootAndCAByUser/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('findAllRootAndCAByUser')")
    public List<MyCertificate> findAllRootAndCAByUser(@PathVariable Integer id) {
        List<MyCertificate> certificates = new ArrayList<>();

        for (MyCertificate c: this.certificateService.findAllRootsAndCA()) {
            if(c.getUser().getId() == id){
                certificates.add(c);
            }
        }
        return certificates;
    }

    @RequestMapping(value="/findIssuerEmailBySerialNumber", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('findIssuerEmailBySerialNumber')")
    public ResponseEntity<?> findIssuerEmailBySerialNumber(@RequestBody RevokeCertificateDTO dto){
        System.out.println("VRACAMOOO: " + certificateService.findIssuerEmailBySerialNumber(dto));
        if(certificateService.findIssuerEmailBySerialNumber(dto) == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(certificateService.findIssuerEmailBySerialNumber(dto), HttpStatus.OK);
    }

    @RequestMapping(value="/findBySerialNumber/{serialNumber}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('findBySerialNumber')")
    public AllCertificatesViewDTO findBySerialNumber(@PathVariable String serialNumber) {
        CertificateMapper certificateMapper = new CertificateMapper();
        return certificateMapper.certificateWithCommonNameToCertificateDto(this.certificateService.findMyCertificateBySerialNumber(serialNumber));
    }
}
