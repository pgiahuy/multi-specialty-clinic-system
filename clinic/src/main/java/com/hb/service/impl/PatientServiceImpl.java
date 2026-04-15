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
import java.time.LocalDate;
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
        return patientRepo.getPatients(params);
    }

    @Override
    public Patient getPatientById(Long id) {
        return this.patientRepo.getPatientById(id);
    }

    @Override
    public Patient addPatient(Map<String,String> params, User user) {
        Patient p = new Patient();
        p.setUserId(user);

        return this.patientRepo.addPatient(p);
    }

    @Override
    public void updateProfile(Long id, Map<String, String> params) {
        Patient p = patientRepo.getPatientById(id);

        if (params.containsKey("phone")) {
            p.setPhone(params.get("phone"));
        }
        if (params.containsKey("gender")) {
            p.setGender(params.get("gender"));
        }
        if (params.containsKey("dob")) {
            String dobStr = params.get("dob");
            try {
                Date dob = new SimpleDateFormat("dd/MM/yyyy").parse(dobStr);
                p.setDob(dob);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        patientRepo.updatePatient(p);
    }
}


