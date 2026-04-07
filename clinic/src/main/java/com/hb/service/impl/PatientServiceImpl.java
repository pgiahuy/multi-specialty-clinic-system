/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.pojo.Patient;
import com.hb.pojo.User;
import com.hb.repository.PatientRepository;
import com.hb.service.PatientService;
import java.io.Serial;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepo;

    @Override
    public List<Patient> getPatients(Map<String, String> params) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Patient getPatientById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Patient addPatient(Map<String, String> params, User u) {
        Patient p = new Patient();
        p.setUserId(u);
        
        p.setPhone(params.get("phone"));
        p.setGender(params.get("gender"));
        
        String dobStr = params.get("dob");
        if (dobStr != null && !dobStr.isEmpty()) {
            try {
                Date dob = new SimpleDateFormat("yyyy-MM-dd").parse(dobStr);
                p.setDob(dob);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.patientRepo.addPatient(p);
    }
    
}
