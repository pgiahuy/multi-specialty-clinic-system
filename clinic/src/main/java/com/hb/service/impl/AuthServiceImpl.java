/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.pojo.User;
import com.hb.repository.UserRepository;
import com.hb.service.AuthService;
import com.hb.service.PatientService;
import com.hb.service.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DELL
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;
    
    @Autowired
    private UserRepository userRepo;
    
    @Override
    @Transactional
    public void registerPatient(Map<String, String> params, MultipartFile avatar) {
        User u = userService.addUser(params, avatar);     
        patientService.addPatient(u);    
    }

    @Override
    public boolean authenticate(String username, String password) {
        return this.userRepo.authenticate(username, password);
    }
}
