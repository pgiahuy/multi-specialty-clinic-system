/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.PatientCreateRequest;
import com.hb.mapper.PatientMapper;
import com.hb.pojo.Patient;
import com.hb.pojo.User;
import com.hb.repository.PatientRepository;
import com.hb.service.PatientService;
import java.io.Serial;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author HUY
 */
@Service
public class PatientServiceImpl implements PatientService {
    
    @Autowired
    private PatientRepository patientRepo;
    
    @Autowired
    private CloudinaryService cloudinaryService;
    
    @Autowired
    private PatientMapper patientMapper;
    
    @Override
    public List<Patient> getPatients(Map<String, String> params) {
        return patientRepo.getPatients(params);
    }
    
    @Override
    public Patient getPatientById(Long id) {
        return this.patientRepo.getPatientById(id);
    }
    
    @Override
    public Patient addPatient(PatientCreateRequest prq, User u) {
        Patient p = patientMapper.toEntiy(prq);
        p.setUser(u);
        return this.patientRepo.addPatient(p);
    }
    
    @Override
    @Transactional
    public void updateProfile(Long id, PatientCreateRequest prq) {
        Patient patient = patientRepo.getPatientById(id);
        patientMapper.updateFromRequest(prq, patient);
        patientRepo.updatePatient(patient);
    }
    
}
