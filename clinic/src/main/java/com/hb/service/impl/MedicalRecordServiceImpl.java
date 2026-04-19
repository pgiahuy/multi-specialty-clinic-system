/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.pojo.MedicalRecord;
import com.hb.repository.AppointmentRepository;
import com.hb.repository.MedicalRecordRepository;
import com.hb.service.MedicalRecordService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepo;
    
    @Autowired
    private AppointmentRepository appointmentRepo;

    @Override
    public MedicalRecord addMedicalRecord(Map<String, String> params) {
        MedicalRecord m = new MedicalRecord();

        m.setDiagnosis(params.getOrDefault("diagnosis", ""));
        m.setNote(params.getOrDefault("note", ""));

        String appointmentIdStr = params.get("appointmentId");
        if (appointmentIdStr != null && !appointmentIdStr.isEmpty()) {
            try {
                Long appointmentId = Long.parseLong(appointmentIdStr);
                var appointment = appointmentRepo.getAppointmentById(appointmentId);
                m.setAppointment(appointment);
                
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid appointment ID format");
            }
        }

        return this.medicalRecordRepo.addMedicalRecord(m);
    }

    @Override
    public List<MedicalRecord> getMedicalRecords(Map<String, String> params) {
        return this.medicalRecordRepo.getMedicalRecords(params);
    }

    @Override
    public MedicalRecord getMedicalRecordById(Long id) {
        return this.medicalRecordRepo.getMedicalRecordById(id);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        this.medicalRecordRepo.deleteMedicalRecord(id);
    }

    @Override
    public long countMedicalRecords(Map<String, String> params) {
        return medicalRecordRepo.count(params, MedicalRecord.class);
    }
}