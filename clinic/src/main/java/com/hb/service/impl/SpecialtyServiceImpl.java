/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.pojo.Doctor;
import com.hb.pojo.Specialtie;
import com.hb.repository.DoctorRepository;
import com.hb.repository.SpecialtieRepository;
import com.hb.service.SpecialtieService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */
@Service
public class SpecialtieServiceImpl implements SpecialtieService {

    @Autowired
    private SpecialtieRepository specialtieRepo;
    
    @Autowired
    private DoctorRepository doctorRepo;

    @Override
    public List<Specialtie> getSpecialties(Map<String, String> params) {
        return this.specialtieRepo.getSpecialties(params);
    }

    @Override
    public Specialtie getSpecialtieById(Long id) {
        Specialtie s = this.specialtieRepo.getSpecialtieById(id);
        if (s == null) {
            throw new RuntimeException("Specialtie not found!");
        }
        return s;
    }

    @Override
    public Specialtie addSpecialtie(Map<String, String> params) {
        Specialtie s = new Specialtie();

        String name = params.get("name");
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Missing name");
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

}
