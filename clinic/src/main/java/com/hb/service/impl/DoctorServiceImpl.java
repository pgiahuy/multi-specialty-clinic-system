/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.form.DoctorForm;
import com.hb.exception.BadRequestException;
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
import java.util.HashSet;

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
    public Doctor saveOrUpdate(DoctorForm doctorForm) {
        Doctor d;
        if (doctorForm.getId() == null) {
            d = new Doctor();
        } else {
            d = this.doctorRepo.getDoctorById(doctorForm.getId());
        }

        d.setDescription(doctorForm.getDescription() != null ? doctorForm.getDescription().trim() : "");
        d.setFullName(doctorForm.getFullName() != null ? doctorForm.getFullName().trim() : "");
        d.setGender(doctorForm.getGender() != null ? doctorForm.getGender().trim() : "");
        d.setCccd(doctorForm.getCccd() != null ? doctorForm.getCccd().trim() : "");
        d.setIsActive(true);

        if (doctorForm.getUserId() != null) {
            User u = this.userRepo.getUserById(doctorForm.getUserId());
            if (u == null) {
                throw new ResourceNotFoundException("User không tồn tại!");
            }
            d.setUserId(u);
        }

        if (doctorForm.getSpecialtyIds() != null && !doctorForm.getSpecialtyIds().isEmpty()) {
            List<Long> ids = doctorForm.getSpecialtyIds();

            List<Specialty> specs = this.specialtieRepo.getAllById(ids);

            if (specs.size() != ids.size())
                throw new ResourceNotFoundException("Một số chuyên khoa không tồn tại!");

            d.setSpecialtyCollection(new HashSet<>(specs));

        } else {
            throw new BadRequestException("Thiếu thông tin chuyên khoa!");
        }
        return this.doctorRepo.saveOrUpdate(d);
    }

    @Override
    public List<Doctor> getDoctors(Map<String, String> params) {
        return doctorRepo.getDoctors(params);
    }
    

    @Override
    public Doctor getDoctorById(Long id) {
        Doctor d = doctorRepo.getDoctorById(id);
        if (d == null) {
            throw new RuntimeException("Không tìm thấy bác sĩ!");
        }
        return d;
    }

    @Override
    public void deleteDoctor(Long id) {
        this.doctorRepo.deleteDoctor(id);
    }

    @Override
    public long countDoctors(Map<String, String> params) {
        return doctorRepo.count(params, Doctor.class);
    }

    @Override
    public Doctor getDoctorByUsername(String username) {
        User u = userRepo.getUserByUsername(username);
        Doctor dr = u.getDoctor();
        return dr;
    }

   
}
