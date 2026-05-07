/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.ScheduleCreateRequest;
import com.hb.dto.response.ScheduleRepsonse;
import com.hb.exception.ResourceNotFoundException;
import com.hb.mapper.ScheduleMapper;
import com.hb.pojo.Doctor;
import com.hb.pojo.Rooms;
import com.hb.pojo.Schedules;
import com.hb.pojo.Shifts;
import com.hb.repository.DoctorRepository;
import com.hb.repository.RoomRepository;
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
    
    @Autowired
    private RoomRepository roomRepo;
    
    @Autowired
    private ScheduleMapper scheduleMapper;
    
    @Override
    public List<Schedules> getSchedules(Map<String, String> params) {
        return this.scheduleRepo.getSchedules(params);
    }

    @Override
    public ScheduleRepsonse addSchedule(ScheduleCreateRequest req) {
        Schedules schedule = new Schedules();

        schedule.setDate(req.getDate());
        

        
        schedule.setMaxPatients(req.getMaxPatients());
        schedule.setCurrentPatients(0);
        
        Doctor doctor = doctorRepo.getDoctorById(req.getDoctorId());
        if (doctor == null)
            throw new ResourceNotFoundException("Không tìm thấy bác sĩ!");
        Shifts shift = shiftRepo.getShiftById(req.getShiftId());
        if (shift == null)
            throw new ResourceNotFoundException("Không tìm thấy ca khám!");
        Rooms room = roomRepo.getRoomById(req.getRoomId());
        if (room == null)
            throw new ResourceNotFoundException("Không tìm thấy phòng!");

        schedule.setDoctorId(doctor);
        schedule.setSpecialtyId(doctor.getSpecialty());
        schedule.setShiftId(shift);
        schedule.setRoomId(room);
        

        Schedules s = this.scheduleRepo.addSchedule(schedule);
        return scheduleMapper.toResponse(s);
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