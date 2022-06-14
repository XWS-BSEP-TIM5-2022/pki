package com.security.pki.controller;

import com.security.pki.dto.*;
import com.security.pki.mapper.CertificateMapper;
import com.security.pki.model.MyCertificate;
import com.security.pki.model.User;
import com.security.pki.service.CertificateService;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    static Logger log = Logger.getLogger(CertificateController.class.getName());

    @RequestMapping(value="", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('getAllCertificates')")
    public List<AllCertificatesViewDTO> getAll() {
        log.info("Successfully found all certificates!");
        return this.certificateService.findAll();
    }

    @RequestMapping(value="/getAllByUser/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('getAllCertificatesByUser')")
    public List<AllCertificatesViewDTO> getAllByUser(@PathVariable Integer id) {
        log.info("Successfully found all certificates by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return this.certificateService.findAllByUser(id);
    }

    @RequestMapping(value="/downloadCertificate/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('downloadCertificate')")
    public ResponseEntity<?> downloadCertificate(@PathVariable Integer id) throws KeyStoreException, CertificateEncodingException, IOException {
        MyCertificate cert = certificateService.findById(id);
        if(cert.isRevoked()){
            log.error("Error while downloading certificate by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Certificate certificate = certificateService.findCertificateBySerialNumber(cert.getSerialNumber(), cert.getCertificateType().toString());
        certificateService.downloadCert(certificate, cert.getSerialNumber());
        log.info("Successfully certificate downloaded by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/findById/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('findCertificateById')")
    public CertificateReviewDTO findById(@PathVariable Integer id) {
        log.info("Successfully found certificate with id: " + id + "by user: " +  SecurityContextHolder.getContext().getAuthentication().getName());

        return this.certificateService.findDtoById(id);
    }

    @RequestMapping(value="/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('createCertificate')")
    public ResponseEntity<?> issueCertificate(@RequestBody CreateCertificateDTO dto) {
        X509Certificate certificate = certificateService.issueCertificate(dto);
        if(certificate == null) {
            log.error("Error while issuing certificate by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.info("Successfully issued certificate by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @RequestMapping(value="/createSelfSigned", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('createSelfSigned')")
    public ResponseEntity<?> createSelfSigned(@RequestBody CreateSelfSignedCertificateDTO dto) {
        X509Certificate certificate = certificateService.issueSelfSignedCertificate(dto);

        if(certificate == null) {
            log.error("Error while creating self signed certificate! Logged user: " + SecurityContextHolder.getContext().getAuthentication().getName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("Successfully created self signed certificate! Logged user: " + SecurityContextHolder.getContext().getAuthentication().getName());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value="/findAllRootsAndCA", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('findAllRootsAndCA')")
    public List<MyCertificate> findAllRootsAndCA() {
        log.info("Successfully found all roots and ca certificates by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return this.certificateService.findAllRootsAndCA();
    }

    @RequestMapping(value="/findUserByCertificateSerialNumber/{serialNumber}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('findUserByCertificateSerialNumber')")
    public User findUserByCertificateSerialNumber(@PathVariable String serialNumber) {
        MyCertificate certificate = this.certificateService.findMyCertificateBySerialNumber(serialNumber);
        log.info("Successfully found certificate with serialNumber" + serialNumber + "  by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return certificate.getUser();
    }


    @RequestMapping(value="/revokeCerificate/{serialNumber}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('revokeCertificate')")
    public void revokeCerificate(@PathVariable String serialNumber){
        log.info("Successfully revoked certificate with serialNumber" + serialNumber + "  by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        certificateService.revokeCerificate(serialNumber);
    }
  
    @RequestMapping(value="/findCertificateBySerialNumber/{serialNumber}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('findCertificateBySerialNumber')")
    public MyCertificate findCertificateBySerialNumber(@PathVariable String serialNumber) {
        log.info("Successfully found certificate with serialNumber" + serialNumber + "  by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
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
        log.info("Successfully found all root and CA certificates by user: " + SecurityContextHolder.getContext().getAuthentication().getName());

        return certificates;
    }

    @RequestMapping(value="/findIssuerEmailBySerialNumber", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('findIssuerEmailBySerialNumber')")
    public ResponseEntity<?> findIssuerEmailBySerialNumber(@RequestBody RevokeCertificateDTO dto){
        System.out.println("VRACAMOOO: " + certificateService.findIssuerEmailBySerialNumber(dto));
        if(certificateService.findIssuerEmailBySerialNumber(dto) == null){
            log.error("Error while finding issuer email with serialNumber" + dto.getSerialNumber() + "  by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("Successfully found issuer email with serialNumber" + dto.getSerialNumber() + "  by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(certificateService.findIssuerEmailBySerialNumber(dto), HttpStatus.OK);
    }

    @RequestMapping(value="/findBySerialNumber/{serialNumber}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('findBySerialNumber')")
    public AllCertificatesViewDTO findBySerialNumber(@PathVariable String serialNumber) {
        CertificateMapper certificateMapper = new CertificateMapper();
        log.info("Successfully found certificate with serialNumber" + serialNumber + "  by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return certificateMapper.certificateWithCommonNameToCertificateDto(this.certificateService.findMyCertificateBySerialNumber(serialNumber));
    }
}
