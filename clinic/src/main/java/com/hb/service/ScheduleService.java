/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.ScheduleCreateRequest;
import com.hb.dto.response.ScheduleRepsonse;
import com.hb.pojo.Schedule;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface ScheduleService {
    List<Schedule> getSchedules(Map<String, String> params);
    ScheduleRepsonse addSchedule(ScheduleCreateRequest req);
    Schedule getScheduleById(Long id);
    void deleteSchedule(Long id);
    long countSchedules(Map<String, String> params);
    
    void checkDoctorAvailability(ScheduleCreateRequest req);
    void checkRoomAvailability(ScheduleCreateRequest req);

}