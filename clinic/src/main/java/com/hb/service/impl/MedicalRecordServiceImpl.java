/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.MedicalRecordCreateRequest;
import com.hb.enums.PrescriptionStatus;
import com.hb.exception.DuplicateResourceException;
import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.Appointment;
import com.hb.pojo.MedicalRecord;
import com.hb.pojo.Patient;
import com.hb.pojo.User;
import com.hb.enums.UserRole;
import com.hb.repository.AppointmentRepository;
import com.hb.repository.DoctorRepository;
import com.hb.repository.MedicalRecordRepository;
import com.hb.repository.PatientRepository;
import com.hb.repository.UserRepository;
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
    private UserRepository userRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Override
    public MedicalRecord addOrUpdateMedicalRecord(MedicalRecordCreateRequest req) {
        MedicalRecord m;

        if (req.getId() != null) {
            m = medicalRecordRepo.getMedicalRecordById(req.getId());
            if (m.getPrescription() != null && m.getPrescription().getStatus() == PrescriptionStatus.PUBLIC) {
                throw new DuplicateResourceException("Hồ sơ bệnh án này đã khám xong, không thể chỉnh sửa!");
            }

        } else {
            m = new MedicalRecord();
            Appointment appointment = appointmentRepo.getAppointmentById(req.getAppointmentId());
            m.setAppointmentId(appointment);
            m.setCreatedAt(new Date());
            appointmentRepo.addOrUpdateAppointment(appointment);
        }
        if (!req.getDiagnosis().isEmpty()) {
            m.setDiagnosis(req.getDiagnosis());
        }

        if (!req.getNote().isEmpty()) {
            m.setNote(req.getNote());
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
        return medicalRecordRepo.countMedicalRecords(params);
    }

    @Override
    public boolean checkAccessControll(String username, Long patientId) {
        Patient p = patientRepo.getPatientById(patientId);
        if (p == null) {
            throw new ResourceNotFoundException("Không tìm thấy bệnh nhân!");
        }

        User currentUser = userRepo.getUserByUsername(username);
        if (currentUser == null) {
            return false;
        }

        if (currentUser.getRole() == UserRole.ROLE_PATIENT) {
            return p.getUserId() != null && p.getUserId().getUsername().equals(username);
        }

        if (currentUser.getRole() == UserRole.ROLE_DOCTOR) {
            return appointmentRepo.existsAppointmentForPatientAndDoctorUser(patientId, currentUser.getId());
        }

        if (currentUser.getRole() == UserRole.ROLE_STAFF) {
            return true;
        }

        return false;
    }

    @Override
    public boolean checkAccessControll(User user, Long medicalRecordId) {
        return this.medicalRecordRepo.checkAccessControll(user, medicalRecordId);
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
