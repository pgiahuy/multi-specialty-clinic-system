/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Schedules;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface ScheduleRepository extends BaseRepository<Schedules>{
    List<Schedules> getSchedules(Map<String,String> params);
    Schedules addSchedule(Schedules d);
    Schedules getScheduleById(Long id);
    void deleteSchedule(Long id);
    
}