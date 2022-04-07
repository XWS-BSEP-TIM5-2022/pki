package com.security.pki.controller;

import com.security.pki.model.Certificate;
import com.security.pki.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@RestController
@RequestMapping(value = "api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @RequestMapping(value="", method = RequestMethod.GET)
    public List<Certificate> getAll() {
        return this.certificateService.findAll();
    }

    @RequestMapping(value="/findById/{id}", method = RequestMethod.GET)
    public Certificate findById(@PathVariable Integer id) {
        return this.certificateService.findById(id);
    }
}
