/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.MedicalRecordCreateRequest;
import com.hb.enums.AppointmentStatus;
import com.hb.exception.ResourceNotFoundException;
import com.hb.mapper.MedicalRecordMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.MedicalRecord;
import com.hb.pojo.Patient;
import com.hb.repository.AppointmentRepository;
import com.hb.repository.MedicalRecordRepository;
import com.hb.repository.PatientRepository;
import com.hb.service.MedicalRecordService;
import java.util.Date;
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
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Override
    public MedicalRecord addOrUpdateMedicalRecord(MedicalRecordCreateRequest req) {
        MedicalRecord m;

        if (medicalRecordRepo.getMedicalRecordById(req.getId()) != null) {
            m = medicalRecordRepo.getMedicalRecordById(req.getId());

            if (!req.getDiagnosis().isEmpty()) {
                m.setDiagnosis(req.getDiagnosis());
            }

            if (!req.getNote().isEmpty()) {
                m.setNote(req.getNote());
            }
        } else {
            m = new MedicalRecord();

            Appointment appointment = appointmentRepo.getAppointmentById(req.getAppointId());
            m.setAppointmentId(appointment);
            m.setCreatedAt(new Date());

            appointment.setStatus(AppointmentStatus.IN_PROGRESS);
            appointmentRepo.addOrUpdateAppointment(appointment);
        }

        return this.medicalRecordRepo.addorUpdateMedicalRecord(m);
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

    @Override
    public boolean checkAccessControll(String username, Long patientId) {
        Patient p = patientRepo.getPatientById(patientId);
        if (p == null) {
            throw new ResourceNotFoundException("Patient not found!");
        }

        return p.getUserId().getUsername().equals(username);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        Patient p = patientRepo.getPatientById(patientId);
        if (p == null) {
            throw new ResourceNotFoundException("Patient not found!");
        }

        List<MedicalRecord> res = medicalRecordRepo.getMedicalRecordsByPatientId(patientId);
        return res;
    }

    @Override
    public MedicalRecord getMedicalRecordByAppointmentId(Long appointmentId) {
        Appointment a = appointmentRepo.getAppointmentById(appointmentId);

        if (a == null) {
            throw new ResourceNotFoundException("Appointment not found!");
        }

        return this.medicalRecordRepo.getMedicalRecordByAppointmentId(appointmentId);

    }
}
