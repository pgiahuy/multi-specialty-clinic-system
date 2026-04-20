/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.AppointmentCreateRequest;
import com.hb.dto.response.AppointmentResponse;
import com.hb.pojo.Appointment;
import com.hb.repository.AppointmentRepository;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface AppointmentService {
    List<Appointment> getAppointments(Map<String,String> params);
    Appointment getAppointmentById(Long id);
    AppointmentResponse addAppointment(AppointmentCreateRequest req);
    long countAppointments(Map<String,String> params);
}
