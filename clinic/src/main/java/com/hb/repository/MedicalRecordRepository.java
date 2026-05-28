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
    long countMedicalRecords(Map<String, String> params);
    MedicalRecord addMedicalRecord(MedicalRecord m);
    MedicalRecord getMedicalRecordById(Long id);
    List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId);
    void deleteMedicalRecord(Long id);
    
}
