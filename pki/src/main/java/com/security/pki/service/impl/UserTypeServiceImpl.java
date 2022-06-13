package com.security.pki.service.impl;

import com.security.pki.model.Permission;
import com.security.pki.model.UserType;
import com.security.pki.repository.UserTypeRepository;
import com.security.pki.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserTypeServiceImpl implements UserTypeService {
    @Autowired
    private UserTypeRepository userTypeRepository;

    @Override
    public UserType findUserTypeByName(String roleUser) {
        return userTypeRepository.findByName(roleUser);
    }

    @Override
    public Set<String> getPermissionsById(Long id) {
        Set<String> permissions = new HashSet<>();
        for(UserType ut: userTypeRepository.findAll()){
            if(ut.getId() == id) {
                Permission[] ps = userTypeRepository.findPermissionsById(id);
                for(Permission p : userTypeRepository.findPermissionsById(id)){
                    permissions.add(p.getName());
                }
            }
        }
        return permissions;
    }
}
