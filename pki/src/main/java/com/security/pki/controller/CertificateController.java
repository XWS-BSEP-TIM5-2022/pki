package com.security.pki.controller;

import com.security.pki.dto.AllCertificatesViewDTO;
import com.security.pki.dto.CertificateDTO;
import com.security.pki.dto.CreateCertificateDTO;
import com.security.pki.model.Certificate;
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

    @RequestMapping(value="/findById/{id}", method = RequestMethod.GET)
    public Certificate findById(@PathVariable Integer id) {
        return this.certificateService.findById(id);
    }

    @RequestMapping(value="/create", method = RequestMethod.POST)
    public ResponseEntity<?> issueCertificate(@RequestBody CreateCertificateDTO dto) {
        X509Certificate certificate = certificateService.issueCertificate(dto);
        if(certificate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        System.out.println("-------------------------------------------------------");
        System.out.println(certificate);
        System.out.println("-------------------------------------------------------");

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
