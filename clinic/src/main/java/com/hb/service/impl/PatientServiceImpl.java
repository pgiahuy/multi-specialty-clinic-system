/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.PatientCreateRequest;
import com.hb.dto.request.form.PatientForm;
import com.hb.dto.response.PatientResponse;
import com.hb.mapper.PatientMapper;
import com.hb.pojo.Patient;
import com.hb.pojo.User;
import com.hb.repository.PatientRepository;
import com.hb.repository.UserRepository;
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
@Transactional
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepo;
    
    @Autowired
    private UserRepository userRepo;


    @Autowired
    private PatientMapper patientMapper;


    @Override
    public List<Patient> getPatients(Map<String, String> params) {
        return patientRepo.getPatients(params);
    }

    @Override
    public List<Patient> getPatientsForDoctor(Map<String, String> params) {
        return patientRepo.getPatientsForDoctor(params);
    }

    @Override
    public Patient getPatientById(Long id) {
        return this.patientRepo.getPatientById(id);
    }

    @Override
    public void deletePatient(Long id) {
        this.patientRepo.deletePatient(id);
    }

    @Override
    public PatientResponse addPatient(PatientCreateRequest prq, User u) {
        Patient p = patientMapper.toEntity(prq, u);
        p.setUserId(u);
        Patient patient = this.patientRepo.saveOrUpdate(p);
        return patientMapper.toResponse(patient);

    }

    @Override
    public PatientResponse updateProfile(Long id, PatientCreateRequest prq) {
        Patient patient = patientRepo.getPatientById(id);
        patientMapper.updateEntity(prq, patient);
        patientRepo.saveOrUpdate(patient);
        return this.patientMapper.toResponse(patient);
    }

    @Override
    public long countPatients(Map<String, String> params) {
        return patientRepo.count(params, Patient.class);
    }

    @Override
    public Patient saveOrUpdate(PatientForm form) {
        Patient p;
        if (form.getId()==null) {
            p = new Patient();
        }else{
            p = this.patientRepo.getPatientById(form.getId());
        }
        
        p.setCccd(form.getCccd()!= null ? form.getCccd() : null);
        p.setFullName(form.getFullName()!= null ? form.getFullName() : null);
        p.setDob(form.getDob()!= null ? form.getDob() : null);
        p.setGender(form.getGender()!= null ? form.getGender(): null);
        p.setAddress(form.getAddress()!= null ? form.getAddress(): null);
        p.setPhone(form.getPhone()!= null ? form.getPhone(): null);
        
        if(form.getUserId()!=null){
            User u = this.userRepo.getUserById(form.getUserId());
            p.setUserId(u);
        }
        
        return this.patientRepo.saveOrUpdate(p);

    }
    
    

    
}
