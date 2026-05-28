/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.MedicalRecordCreateRequest;
import com.hb.pojo.MedicalRecord;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface MedicalRecordService {
    MedicalRecord addOrUpdateMedicalRecord(MedicalRecordCreateRequest req);
    List<MedicalRecord> getMedicalRecords(Map<String, String> params);
    MedicalRecord getMedicalRecordById(Long id);
    MedicalRecord getMedicalRecordByAppointmentId(Long appointmentId);
    List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId);
    void deleteMedicalRecord(Long id);
    long countMedicalRecords(Map<String, String> params);
    boolean checkAccessControll (String username, Long patientId);
}