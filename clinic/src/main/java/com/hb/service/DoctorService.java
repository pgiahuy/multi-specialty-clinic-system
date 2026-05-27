/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.form.DoctorForm;
import com.hb.pojo.Doctor;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface DoctorService {
    Doctor saveOrUpdate(DoctorForm doctorForm);
    List<Doctor> getDoctors(Map<String,String> params);
    Doctor getDoctorById(Long id);
    Doctor getDoctorByUsername(String username);
    void deleteDoctor(Long id);
    long countDoctors(Map<String,String> params);
}
