package com.security.pki.service;

import com.security.pki.dto.AllCertificatesViewDTO;
import com.security.pki.dto.CertificateDTO;
import com.security.pki.mapper.CertificateMapper;
import com.security.pki.model.Certificate;
import com.security.pki.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;


    private CertificateMapper certificateMapper = new CertificateMapper();


    public List<AllCertificatesViewDTO> findAll() {
        List<AllCertificatesViewDTO> dtos  = new ArrayList<>();
        for(Certificate c : this.certificateRepository.findAll()){
            dtos.add(this.certificateMapper.certificateWithCommonNameToCertificateDto(c));
        }
        return dtos;
    }

    public Certificate findById(Integer id){
        return this.certificateRepository.findById(id).orElseGet(null);
    }

    public List<AllCertificatesViewDTO> findAllByUser(Integer id) {
        List<AllCertificatesViewDTO> dtos  = new ArrayList<>();
        for(Certificate c : this.certificateRepository.findAll()){
            if(c.getUser().getId() == id){
                dtos.add(this.certificateMapper.certificateWithCommonNameToCertificateDto(c));
            }
        }
        return dtos;
    }
}
