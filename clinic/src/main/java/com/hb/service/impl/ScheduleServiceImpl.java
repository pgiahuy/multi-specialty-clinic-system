/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.ScheduleCreateRequest;
import com.hb.dto.response.ScheduleRepsonse;
import com.hb.exception.BadRequestException;
import com.hb.exception.DuplicateResourceException;
import com.hb.exception.ResourceNotFoundException;
import com.hb.mapper.ScheduleMapper;
import com.hb.pojo.Doctor;
import com.hb.pojo.Room;
import com.hb.pojo.Schedule;
import com.hb.pojo.Shift;
import com.hb.pojo.Specialty;
import com.hb.repository.DoctorRepository;
import com.hb.repository.RoomRepository;
import com.hb.repository.ScheduleRepository;
import com.hb.repository.ShiftRepository;
import com.hb.repository.SpecialtyRepository;
import com.hb.service.ScheduleService;
import java.time.LocalDate;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private SpecialtyRepository specialtyRepo;
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public List<Schedule> getSchedules(Map<String, String> params) {
        return this.scheduleRepo.getSchedules(params);
    }

    @Override
    @Transactional
    public ScheduleRepsonse addSchedule(ScheduleCreateRequest req) {
        if (req.getDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Ngày đăng ký không hợp lệ!");
        }

        Doctor doctor = doctorRepo.getDoctorById(req.getDoctorId());
        if (doctor == null) {
            throw new ResourceNotFoundException("Không tìm thấy bác sĩ!");
        }
        Shift shift = shiftRepo.getShiftById(req.getShiftId());
        if (shift == null) {
            throw new ResourceNotFoundException("Không tìm thấy ca khám!");
        }
        Room room = roomRepo.getRoomById(req.getRoomId());
        if (room == null) {
            throw new ResourceNotFoundException("Không tìm thấy phòng!");
        }
        Specialty specialty = specialtyRepo.getSpecialtieById(req.getSpecialtyId());
        if (specialty == null) {
            throw new ResourceNotFoundException("Không tìm thấy chuyên khoa!");
        }

        boolean isSpecialtyMatch = doctor.getSpecialtyCollection().stream()
                .anyMatch(spec -> spec.getId().equals(req.getSpecialtyId()));

        if (!isSpecialtyMatch) {
            throw new BadRequestException("Bác sĩ " + doctor.getFullName() + " không thuộc chuyên khoa này!");
        }

        if (room.getSpecialtyId() != null && !room.getSpecialtyId().getId().equals(req.getSpecialtyId())) {
            throw new BadRequestException("Phòng này không thuộc chuyên khoa của ca khám!");
        }

        boolean isDoctorAvailable = this.scheduleRepo.checkDoctorAvailability(doctor.getId(), req.getDate(), shift.getId(), null);
        if (!isDoctorAvailable) {
            throw new DuplicateResourceException("Bác sĩ " + doctor.getFullName() + " đã có lịch trực vào ca này trong ngày rồi!");
        }

        boolean isRoomAvailable = this.scheduleRepo.checkRoomAvailability(room.getId(), req.getDate(), shift.getId(), null);
        if (!isRoomAvailable) {
            throw new DuplicateResourceException("Phòng " + room.getRoomNumber() + " đã được xếp cho ca trực khác mất rồi!");
        }

        Schedule schedule = new Schedule();
        schedule.setDate(req.getDate());
        schedule.setMaxPatients(req.getMaxPatients());
        schedule.setCurrentPatients(0);

        schedule.setDoctorId(doctor);
        schedule.setSpecialtyId(specialty);
        schedule.setShiftId(shift);
        schedule.setRoomId(room);

        Schedule s = this.scheduleRepo.saveOrUpdate(schedule);
        return scheduleMapper.toResponse(s);
    }

    @Override
    public Schedule getScheduleById(Long id) {
        return this.scheduleRepo.getScheduleById(id);
    }
    
    @Override
    public Schedule getScheduleByDoctor(Map<String, String> params) {
        return this.scheduleRepo.getScheduleByDoctor(params);
    }

    @Override
    public void deleteSchedule(Long id) {
        this.scheduleRepo.deleteSchedule(id);
    }

    @Override
    public long countSchedules(Map<String, String> params) {
        return scheduleRepo.count(params, Schedule.class);
    }

    

}
