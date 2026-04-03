/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Patient;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface  PatientRepository {
    List<Patient> getPatients(Map<String,String> params);
    Patient getPatientById(Long id);
    Patient addPatient(Patient p);
}
