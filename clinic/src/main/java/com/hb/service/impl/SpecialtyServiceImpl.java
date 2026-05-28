/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.form.SpecialtyForm;
import com.hb.exception.BadRequestException;
import com.hb.pojo.Doctor;
import com.hb.pojo.Specialty;
import com.hb.repository.DoctorRepository;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hb.repository.SpecialtyRepository;
import com.hb.service.SpecialtyService;
import java.util.Optional;

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
    public Specialty saveOrUpdate(SpecialtyForm form) {
        Specialty s;
        if (form.getId() == null) {
            s = new Specialty();
        } else {
            s = this.getSpecialtieById(form.getId());
        }

        if (form.getName() != null || !form.getName().isEmpty()) {
            s.setName(form.getName());
        } else {
            throw new BadRequestException("Thiếu tên khoa!");
        }

        if (form.getPrice() != null) {
            s.setPrice(form.getPrice());
        } else {
            throw new BadRequestException("Thiếu phí khám bệnh!");
        }

        if (form.getHodId() != null) {
            
            Doctor d = doctorRepo.getDoctorById(form.getHodId());
            s.setIdHod(d);
        }

        return specialtieRepo.saveOrUpdate(s);
    }

    @Override
    public void deleteSpecialty(Long id) {
        this.specialtieRepo.delete(id);
    }

    @Override
    public long countSpecialties(Map<String, String> params) {
        return specialtieRepo.count(params, Specialty.class);
    }

   

}
