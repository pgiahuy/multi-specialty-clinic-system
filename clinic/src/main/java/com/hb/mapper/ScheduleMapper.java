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
    public ScheduleRepsonse toResponse(Schedules sche){
        ScheduleRepsonse s = new ScheduleRepsonse();
        s.setId(sche.getId());
        s.setDate(sche.getDate());
        s.setMaxPatients(sche.getMaxPatients());
        s.setCurrentPatients(sche.getCurrentPatients());
        s.setDoctorName(sche.getDoctorId().getFullName());
        s.setRoom(sche.getRoomId().getRoomNumber());
        s.setArea(sche.getRoomId().getAreaId().getAreaName());
        s.setShiftStartTime(sche.getShiftId().getStartTime().toString());
        s.setShiftEndTime(sche.getShiftId().getEndTime().toString());
    
        return s;
    }
    
    
}
