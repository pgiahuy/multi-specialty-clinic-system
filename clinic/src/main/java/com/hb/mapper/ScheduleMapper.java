/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.request.ScheduleCreateRequest;
import com.hb.dto.response.ScheduleRepsonse;
import com.hb.pojo.Schedule;
import org.springframework.stereotype.Component;

/**
 *
 * @author HUY
 */

@Component
public class ScheduleMapper {
    public ScheduleRepsonse toResponse(Schedule schedule) {
    ScheduleRepsonse s = new ScheduleRepsonse();

    s.setId(schedule.getId());
    s.setDate(schedule.getDate());
    s.setMaxPatients(schedule.getMaxPatients());
    s.setCurrentPatients(schedule.getCurrentPatients());

    if (schedule.getDoctorId() != null) {
        s.setDoctorName(schedule.getDoctorId().getFullName());
    }

    if (schedule.getRoomId() != null) {
        s.setRoom(schedule.getRoomId().getRoomNumber());

        if (schedule.getRoomId().getAreaId() != null) {
            s.setArea(schedule.getRoomId().getAreaId().getAreaName());
        }
    }

    if (schedule.getSpecialtyId() != null) {
        s.setSpecialtyName(schedule.getSpecialtyId().getName());
    }

    if (schedule.getShiftId() != null) {

        if (schedule.getShiftId().getSession() != null) {
            s.setSession(
                schedule.getShiftId().getSession().getLabel()
            );
        }

        if (schedule.getShiftId().getStartTime() != null) {
            s.setShiftStartTime(
                schedule.getShiftId().getStartTime().toString()
            );
        }

        if (schedule.getShiftId().getEndTime() != null) {
            s.setShiftEndTime(
                schedule.getShiftId().getEndTime().toString()
            );
        }
    }

    return s;
}
    
}
