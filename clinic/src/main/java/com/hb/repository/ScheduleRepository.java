/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Schedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface ScheduleRepository extends BaseRepository<Schedule>{
    List<Schedule> getSchedules(Map<String,String> params);
    Schedule saveOrUpdate(Schedule d);
    Schedule getScheduleById(Long id);
    void deleteSchedule(Long id);
    boolean checkDoctorAvailability(Long doctorId, LocalDate date, Long shiftId, Long excludeId);
    boolean checkRoomAvailability(Long roomId, LocalDate date, Long shiftId, Long excludeId);
    int incrementCurrentPatients(Long scheduleId);
    
}