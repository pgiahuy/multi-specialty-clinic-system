/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.AppointmentCreateRequest;
import com.hb.dto.response.AppointmentResponse;
import com.hb.exception.ResourceNotFoundException;
import com.hb.mapper.AppointmentMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.Doctor;
import com.hb.pojo.Patient;
import com.hb.repository.AppointmentRepository;
import com.hb.repository.DoctorRepository;
import com.hb.repository.PatientRepository;
import com.hb.service.AppointmentService;
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
public class AppointmentServiceImpl implements AppointmentService {

    
    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;
    
    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public List<Appointment> getAppointments(Map<String, String> params) {
        return this.appointmentRepo.getAppointments(params);
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        Appointment a = appointmentRepo.getAppointmentById(id);
        if (a == null) {
            throw new ResourceNotFoundException("Appointment not found!");
        }
        return a;
    }

    @Override
    public AppointmentResponse addAppointment(AppointmentCreateRequest req) {

        Appointment a = new Appointment();

        Date date = req.getDate();
        String timeSlot = req.getTimeSlot();

        Doctor doctor = doctorRepo.getDoctorById(req.getDoctorId());
        if (doctor == null){
            throw new ResourceNotFoundException("Doctor not found!");
        }
        Patient patient = patientRepo.getPatientById(req.getPatientId());
        if (patient == null){
            throw new ResourceNotFoundException("Patient not found!");
        }
        
        a.setDoctor(doctor);
        a.setPatient(patient);
        
        a.setDate(date);
        a.setTimeSlot(timeSlot);
        a.setStatus("PENDING");
        a.setCreatedAt(new Date());

        Appointment appointment =  appointmentRepo.addAppointment(a);
        return appointmentMapper.toResponse(appointment);
//        return null;
}

    @Override
    public long countAppointments(Map<String, String> params) {
        return appointmentRepo.count(params, Appointment.class);
    }

}
