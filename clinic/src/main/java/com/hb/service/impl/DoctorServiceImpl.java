/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.Doctor;
import com.hb.pojo.Specialty;
import com.hb.pojo.User;
import com.hb.repository.DoctorRepository;
import com.hb.repository.UserRepository;
import com.hb.service.DoctorService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hb.repository.SpecialtyRepository;

/**
 *
 * @author HUY
 */
@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;
    
    @Autowired
    private SpecialtyRepository specialtieRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public Doctor addDoctor(Map<String, String> params) {
        Doctor d = new Doctor();

        d.setDescription(params.getOrDefault("description", ""));

        String username = params.get("username");
        if (username != null && !username.isEmpty()) {
            User u = this.userRepo.getUserByUsername(username);
            if (u == null) {
                throw new ResourceNotFoundException("User not found");
            }
            d.setUser(u);
        } else {
            throw new RuntimeException("Missing username");
        }

        String specIdStr = params.get("specialtyId");
        if (specIdStr != null && !specIdStr.isEmpty()) {
            Long specId = Long.valueOf(specIdStr);
            Specialty s = this.specialtieRepo.getSpecialtieById(specId);
            if (s == null) {
                throw new ResourceNotFoundException("Specialty not found");
            }
            d.setIdSpecailty(s);
        } else {
            throw new RuntimeException("Missing specialty");
        }

        return this.doctorRepo.addDoctor(d);
    }

    @Override
    public List<Doctor> getDoctors(Map<String, String> params) {
        return doctorRepo.getDoctors(params);
    }

    @Override
    public Doctor getDoctorById(Long id) {
        Doctor d = doctorRepo.getDoctorById(id);
        if (d == null) {
            throw new RuntimeException("Doctor not found!");
        }
        return d;
    }

    @Override
    public void deleteDoctor(Long id) {
        this.doctorRepo.deleteDoctor(id);
    }

}
