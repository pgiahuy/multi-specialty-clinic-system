/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.pojo.Doctor;
import com.hb.pojo.Rooms;
import com.hb.pojo.Schedules;
import com.hb.pojo.Shifts;
import com.hb.repository.DoctorRepository;
import com.hb.repository.ScheduleRepository;
import com.hb.repository.ShiftRepository;
import com.hb.service.ScheduleService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */

@Service
public class ScheduleServiceImpl implements ScheduleService {
    
    @Autowired
    private ScheduleRepository scheduleRepo;
    
    @Autowired
    private DoctorRepository doctorRepo;
    
    @Autowired
    private ShiftRepository shiftRepo;
    
//    @Autowired
//    private Rooms
    
    @Override
    public List<Schedules> getSchedules(Map<String, String> params) {
        return this.scheduleRepo.getSchedules(params);
    }

    @Override
    public Schedules addSchedule(Map<String, String> params) {
        Schedules schedule = new Schedules();

        String dateStr = params.get("date");
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                schedule.setDate(date);
            } catch (Exception e) {
                throw new RuntimeException("Invalid date format");
            }
        }

        String maxPatientsStr = params.get("maxPatients");
        if (maxPatientsStr != null && !maxPatientsStr.isEmpty()) {
            schedule.setMaxPatients(Integer.valueOf(maxPatientsStr));
        }

        String currentPatientsStr = params.get("currentPatients");
        if (currentPatientsStr != null && !currentPatientsStr.isEmpty()) {
            schedule.setCurrentPatients(Integer.valueOf(currentPatientsStr));
        }

        String doctorIdStr = params.get("doctorId");
        if (doctorIdStr != null && !doctorIdStr.isEmpty()) {
            Doctor doctor = doctorRepo.getDoctorById(Long.valueOf(doctorIdStr));
            schedule.setDoctor(doctor);
        }

        String shiftIdStr = params.get("shiftId");
        if (shiftIdStr != null && !shiftIdStr.isEmpty()) {
            Shifts shift = shiftRepo.getShiftById(Long.valueOf(shiftIdStr));
            schedule.setShiftId(shift);
        }

//         String roomIdStr = params.get("roomId");
//         if (roomIdStr != null && !roomIdStr.isEmpty()) {
//             Rooms room = roomRepo.getRoomById(Long.valueOf(roomIdStr));
//             schedule.setRoomId(room);
//         }

        return this.scheduleRepo.addSchedule(schedule);
    }

    @Override
    public Schedules getScheduleById(Long id) {
        return this.scheduleRepo.getScheduleById(id);
    }

    @Override
    public void deleteSchedule(Long id) {
        this.scheduleRepo.deleteSchedule(id);
    }

    @Override
    public long countSchedules(Map<String, String> params) {
        return scheduleRepo.count(params, Schedules.class);
    }
    
}