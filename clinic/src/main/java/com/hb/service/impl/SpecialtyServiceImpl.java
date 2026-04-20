/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.Doctor;
import com.hb.pojo.Specialty;
import com.hb.repository.DoctorRepository;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hb.repository.SpecialtyRepository;
import com.hb.service.SpecialtyService;

/**
 *
 * @author HUY
 */
@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    @Autowired
    private SpecialtyRepository specialtieRepo;
    
    @Autowired
    private DoctorRepository doctorRepo;

    @Override
    public List<Specialty> getSpecialties(Map<String, String> params) {
        return this.specialtieRepo.getSpecialties(params);
    }

    @Override
    public Specialty getSpecialtieById(Long id) {
        Specialty s = this.specialtieRepo.getSpecialtieById(id);
        if (s == null) {
            throw new RuntimeException("Specialtie not found!");
        }
        return s;
    }

    @Override
    public Specialty addSpecialtie(Map<String, String> params) {
        Specialty s = new Specialty();

        String name = params.get("name");
        if (name == null || name.isEmpty()) {
            throw new ResourceNotFoundException("Missing name");
        }
        s.setName(name);

        String hodIdStr = params.get("hodId");
        if (hodIdStr != null && !hodIdStr.isEmpty()) {
            Long hodId = Long.valueOf(hodIdStr);
            Doctor d = doctorRepo.getDoctorById(hodId);
            s.setIdHod(d);
        }

        return specialtieRepo.addSpecialtie(s);
    }

    @Override
    public void deleteDoctor(Long id) {
        this.doctorRepo.deleteDoctor(id);
    }

    @Override
    public long countSpecialties(Map<String, String> params) {
        return specialtieRepo.count(params, Specialty.class);
    }

}
