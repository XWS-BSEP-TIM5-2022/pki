package com.security.pki.service;

import com.security.pki.model.Certificate;
import com.security.pki.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    public List<Certificate> findAll() {
        return this.certificateRepository.findAll();
    }

    public Certificate findById(Integer id){
        return this.certificateRepository.findById(id).orElseGet(null);
    }
}
