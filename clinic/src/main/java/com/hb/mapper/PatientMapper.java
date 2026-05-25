/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.request.PatientCreateRequest;
import com.hb.dto.response.PatientResponse;
import com.hb.pojo.Patient;
import com.hb.pojo.User;

import org.springframework.stereotype.Component;

/**
 *
 * @author DELL
 */
@Component
public class PatientMapper {

      public PatientResponse toResponse(Patient p) {
        if (p == null) {
            return null;
        }

        PatientResponse res = new PatientResponse();

        res.setId(p.getId());
        res.setCccd(p.getCccd());
        res.setFullName(p.getFullName());
        res.setPhone(p.getPhone());
        res.setDob(p.getDob());
        res.setAddress(p.getAddress());
        res.setGender(p.getGender());
//
//        if (p.getUserId() != null) {
//            res.setEmail(p.getUserId().getEmail());
//            res.setAvatar(p.getUserId().getSecureUrl());
//        }

        return res;
    }

    public Patient toEntity(PatientCreateRequest req, User user) {
        if (req == null) {
            return null;
        }

        Patient p = new Patient();

        p.setUserId(user);
    
        p.setCccd(req.getCccd());
        p.setFullName(req.getFullName());
        p.setPhone(req.getPhone());
        p.setDob(req.getDob());
        p.setAddress(req.getAddress());
        p.setGender(req.getGender());

        return p;
    }

}
