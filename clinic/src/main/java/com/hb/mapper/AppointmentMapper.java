/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.request.AppointmentCreateRequest;
import com.hb.dto.response.AppointmentResponse;
import com.hb.enums.AppointmentStatus;
import com.hb.pojo.Appointment;
import com.hb.pojo.Patient;
import com.hb.pojo.Schedules;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 *
 * @author HUY
 */
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse toResponse(Appointment a) {
        if (a == null) {
            return null;
        }

        AppointmentResponse res = new AppointmentResponse();
        res.setId(a.getId());
        res.setStatus(a.getStatus());
        res.setCreatedAt(a.getCreatedAt());

        if (a.getPatientId() != null) {
            res.setPatientFullName(a.getPatientId().getFullName());
        }

        if (a.getScheduleId() != null) {
            var s = a.getScheduleId();
            res.setAppointmentDate(new SimpleDateFormat("dd/MM/yyyy").format(s.getDate()));

            if (s.getDoctorId() != null) {
                res.setDoctorFullName(s.getDoctorId().getFullName());
                res.setSpecialtyName(s.getSpecialtyId().getName());
            }

            if (s.getShiftId() != null) {
                var shift = s.getShiftId();
                res.setSession(shift.getSession().toString());
                res.setTimeSlot(shift.getStartTime() + " - " + shift.getEndTime());
            }

            if (s.getRoomId() != null) {
                var room = s.getRoomId();
                res.setRoomName("Phòng " + room.getRoomNumber());

                if (room.getAreaId() != null) {
                    var area = room.getAreaId();
                    res.setAreaName(area.getAreaName() + " - Tầng " + area.getLocationFloor());
                }
            }
        }

        return res;
    }

    public Appointment toEntity(AppointmentCreateRequest req, Patient patient, Schedules  schedule) {
        if (req == null) {
            return null;
        }

        Appointment a = new Appointment();

        a.setPatientId(patient);   
        a.setScheduleId(schedule);

        a.setStatus(AppointmentStatus.PENDING.name()); 
        a.setCreatedAt(LocalDateTime.now());

        return a;
    }
}
