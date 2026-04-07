/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.Patient;
import com.hb.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface PatientService {
    List<Patient> getPatients(Map<String,String> params);
    Patient getPatientById(Long id);
    Patient addPatient(Map<String,String> params, User u);
}
