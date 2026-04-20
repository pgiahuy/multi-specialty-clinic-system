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
import com.hb.pojo.Schedules;
import com.hb.repository.AppointmentRepository;
import com.hb.repository.DoctorRepository;
import com.hb.repository.PatientRepository;
import com.hb.repository.ScheduleRepository;
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
    private ScheduleRepository scheduleRepo;
    
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

      
        Patient patient = patientRepo.getPatientById(req.getPatientId());
        if (patient == null){
            throw new ResourceNotFoundException("Patient not found!");
        }
        Schedules schedule = scheduleRepo.getScheduleById(req.getScheduleId());
        if (schedule == null){
            throw new ResourceNotFoundException("Schedule not found!");
        }
        

        if (schedule.getCurrentPatients() >= schedule.getMaxPatients()) {
            throw new RuntimeException("Rất tiếc, ca khám này đã đủ số lượng người đăng ký!");
        }

        schedule.setCurrentPatients(schedule.getCurrentPatients() + 1);
        scheduleRepo.addSchedule(schedule);


        Appointment appointment = appointmentMapper.toEntity(req, patient, schedule);


        Appointment savedApp = appointmentRepo.addAppointment(appointment);


        return appointmentMapper.toResponse(savedApp);
        
}

    @Override
    public long countAppointments(Map<String, String> params) {
        return appointmentRepo.count(params, Appointment.class);
    }

}
