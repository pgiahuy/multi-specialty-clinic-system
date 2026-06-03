/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Appointment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface AppointmentRepository extends BaseRepository<Appointment>{
    
    List<Appointment> getAppointments(Map<String,String> params);
    Appointment getAppointmentById(Long id);
    List<Appointment> getAppointmentByPatientId(Long patientId, Map<String, String> params);
    boolean existsAppointmentForPatientAndDoctorUser(Long patientId, Long doctorUserId);
    Appointment addOrUpdateAppointment(Appointment a);
    long countAppointments(Map<String, String> params);
    List<com.hb.dto.response.DoctorRankingResponse> getTopDoctorsByAppointmentCount(int limit, java.time.LocalDate fromDate, java.time.LocalDate toDate);
    List<com.hb.dto.response.DoctorRankingResponse> getTopDoctorsByConvertedAppointmentCount(int limit, java.time.LocalDate fromDate, java.time.LocalDate toDate);
    boolean isPatientAlreadyBookedInSchedule(Long patientId, Long scheduleId);
    List<Appointment> getConfirmedAppointmentsForReminder(LocalDateTime from, LocalDateTime to);
    
}
