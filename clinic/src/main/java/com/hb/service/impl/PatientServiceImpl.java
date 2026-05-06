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
    public PatientResponse addPatient(PatientCreateRequest prq, User u) {
        Patient p = patientMapper.toEntity(prq, u);
        p.setUserId(u);
        Patient patient =  this.patientRepo.addPatient(p);
        return patientMapper.toResponse(patient);
 
    }
    
    @Override
    public void updateProfile(Long id, PatientCreateRequest prq) {
        Patient patient = patientRepo.getPatientById(id);
        patientMapper.toEntity(prq, patient.getUserId());
        patientRepo.updatePatient(patient);
    }

    @Override
    public long countPatients(Map<String, String> params) {
        return patientRepo.count(params, Patient.class);
    }

    
    
}
