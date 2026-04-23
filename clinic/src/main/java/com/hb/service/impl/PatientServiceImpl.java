/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.PatientCreateRequest;
import com.hb.dto.response.PatientResponse;
import com.hb.mapper.PatientMapper;
import com.hb.pojo.Patient;
import com.hb.pojo.User;
import com.hb.repository.PatientRepository;
import com.hb.service.PatientService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
<<<<<<< HEAD
    public PatientResponse addPatient(PatientCreateRequest prq, User u) {
        
        Patient p = patientMapper.toEntity(prq, u);
        this.patientRepo.addPatient(p);
        return this.patientMapper.toResponse(p);
=======
    public Patient addPatient(PatientCreateRequest prq, User u) {
        Patient p = patientMapper.toEntiy(prq);
        p.setUserId(u);
        return this.patientRepo.addPatient(p);
>>>>>>> b22e0c14 (fix api momopayment)
    }
    
//    @Override
//    @Transactional
//    public void updateProfile(Long id, PatientCreateRequest prq) {
//        Patient patient = patientRepo.getPatientById(id);
//        patientMapper.toEntity(prq, patient);
//        patientRepo.updatePatient(patient);
//    }

    @Override
    public long countPatients(Map<String, String> params) {
        return patientRepo.count(params, Patient.class);
    }

    @Override
    public void updateProfile(Long id, PatientCreateRequest prq) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
