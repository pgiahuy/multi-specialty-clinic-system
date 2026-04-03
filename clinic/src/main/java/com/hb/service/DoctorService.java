/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.Doctor;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface DoctorService {
    Doctor addDoctor(Map<String,String> params);
    List<Doctor> getDoctors(Map<String,String> params);
    Doctor getDoctorById(Long id);
}
