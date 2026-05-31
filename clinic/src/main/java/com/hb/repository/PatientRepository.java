/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.enums.PatientRelationship;
import com.hb.pojo.Patient;
import com.hb.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface  PatientRepository extends BaseRepository<Patient>{
    List<Patient> getPatients(Map<String,String> params);
    List<Patient> getPatientsByUserId(Long userId);
    Patient getPatientById(Long id);
    Patient saveOrUpdate(Patient p);
    void deletePatient(Long id);
    
    boolean isExistedCCCD(String cccd);
    boolean isExistedForSelf(User u);
}
