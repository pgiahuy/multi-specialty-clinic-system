/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.request.ScheduleCreateRequest;
import com.hb.dto.response.ScheduleRepsonse;
import com.hb.pojo.Schedules;
import org.springframework.stereotype.Component;

/**
 *
 * @author HUY
 */

@Component
public class ScheduleMapper {
    public ScheduleRepsonse toResponse(Schedules sche) {
    ScheduleRepsonse s = new ScheduleRepsonse();

    s.setId(sche.getId());
    s.setDate(sche.getDate());
    s.setMaxPatients(sche.getMaxPatients());
    s.setCurrentPatients(sche.getCurrentPatients());

    if (sche.getDoctorId() != null) {
        s.setDoctorName(sche.getDoctorId().getFullName());
    }

    if (sche.getRoomId() != null) {
        s.setRoom(sche.getRoomId().getRoomNumber());

        if (sche.getRoomId().getAreaId() != null) {
            s.setArea(sche.getRoomId().getAreaId().getAreaName());
        }
    }

    if (sche.getSpecialtyId() != null) {
        s.setSpecialtyName(sche.getSpecialtyId().getName());
    }

    if (sche.getShiftId() != null) {

        if (sche.getShiftId().getSession() != null) {
            s.setSession(
                sche.getShiftId().getSession().getLabel()
            );
        }

        if (sche.getShiftId().getStartTime() != null) {
            s.setShiftStartTime(
                sche.getShiftId().getStartTime().toString()
            );
        }

        if (sche.getShiftId().getEndTime() != null) {
            s.setShiftEndTime(
                sche.getShiftId().getEndTime().toString()
            );
        }
    }

    return s;
}
    
}
