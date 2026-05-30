/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Appointment;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface AppointmentRepository extends BaseRepository<Appointment>{
    
    List<Appointment> getAppointments(Map<String,String> params);
    long countAppointments(Map<String, String> params);
    Appointment getAppointmentById(Long id);
    List<Appointment> getAppointmentByPatientId(Long patientId, Map<String, String> params);
    void addOrUpdateAppointment(Appointment a);
    boolean isPatientAlreadyBookedInSchedule(Long patientId, Long scheduleId);
}
