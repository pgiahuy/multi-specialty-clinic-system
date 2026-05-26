/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Doctor;
import com.hb.pojo.Specialty;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface DoctorRepository extends BaseRepository<Doctor>{
    List<Doctor> getDoctors(Map<String,String> params);
    List<Doctor> getAllDoctors();
    Doctor saveOrUpdate(Doctor d);
    Doctor getDoctorById(Long id);
    void deleteDoctor(Long id);
}
