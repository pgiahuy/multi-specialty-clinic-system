/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.DoctorResponse;
import com.hb.pojo.Doctor;
import org.springframework.stereotype.Component;

/**
 *
 * @author DELL
 */
@Component
public class DoctorMapper {

    public DoctorResponse toResponse(Doctor d) {
        if (d == null) {
            return null;
        }

        DoctorResponse res = new DoctorResponse();
        
        res.setId(d.getId());
        res.setFullName(d.getFullName());
        res.setDescription(d.getDescription());
        res.setGender(d.getGender());

        
        if (d.getSpecialty() != null) {
            res.setSpecialty(d.getSpecialty().getName());
        } else if (d.getSpecialtyCollection() != null && !d.getSpecialtyCollection().isEmpty()) {
            
            res.setSpecialty(d.getSpecialtyCollection().iterator().next().getName());
        }

       
        if (d.getUserId() != null) {
            res.setEmail(d.getUserId().getEmail());
            res.setAvatar(d.getUserId().getSecureUrl());
        }

        return res;
    }
}
