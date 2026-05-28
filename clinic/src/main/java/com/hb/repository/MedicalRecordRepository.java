/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.MedicalRecord;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface MedicalRecordRepository extends BaseRepository<MedicalRecord>{
    List<MedicalRecord> getMedicalRecords(Map<String,String> params);
    MedicalRecord addorUpdateMedicalRecord(MedicalRecord m);
    MedicalRecord getMedicalRecordById(Long id);
    MedicalRecord getMedicalRecordByAppointmentId(Long appointmentId);
    List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId);
    void deleteMedicalRecord(Long id);
    
}
