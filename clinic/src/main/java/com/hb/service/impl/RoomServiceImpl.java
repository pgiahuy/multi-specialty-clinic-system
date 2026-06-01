/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.form.RoomForm;
import com.hb.dto.response.RoomResponse;
import com.hb.enums.UserRole;
import com.hb.exception.BadRequestException;
import com.hb.exception.ForbiddenException;
import com.hb.exception.ResourceNotFoundException;
import com.hb.mapper.RoomMapper;
import com.hb.pojo.Doctor;
import com.hb.pojo.Room;
import com.hb.pojo.User;
import com.hb.repository.RoomRepository;
import com.hb.service.RoomService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hb.repository.AreaRepository;
import com.hb.repository.DoctorRepository;

/**
 *
 * @author DELL
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private AreaRepository areaRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Override
    public List<Room> getRooms(Map<String, String> params) {
        return roomRepo.getRooms(params);
    }

    @Override
    public List<RoomResponse> getAvailableRoomsForDoctor(Map<String, String> params, User currentUser) {

        if (params == null || !params.containsKey("doctorId") || params.get("doctorId").isEmpty()) {
            throw new BadRequestException("Thiếu thông tin Bác sĩ để tìm phòng!");
        }
        Long doctorId = Long.valueOf(params.get("doctorId"));
        Long targetSpecialtyId = Long.valueOf(params.get("specialtyId"));

        Doctor doctor = doctorRepo.getDoctorById(doctorId);
        
        if (doctor == null) {
            throw new ResourceNotFoundException("Không tìm thấy bác sĩ!");
        }
        
        if (currentUser.getRole() == UserRole.ROLE_DOCTOR) {
            Doctor currentDoctor = currentUser.getDoctor();
            if (currentDoctor == null || !currentDoctor.getId().equals(doctorId)) {
                throw new ForbiddenException("Bạn không có quyền tìm phòng trống cho bác sĩ khác!");
            }
        }

        boolean isIn = doctor.getSpecialtyCollection().stream()
                .anyMatch(spec -> spec.getId().equals(targetSpecialtyId));

        if (!isIn) {
            throw new BadRequestException("Bác sĩ không thuộc khoa đang chọn!");
        }

        List<Room> rooms = roomRepo.getAvailableRooms(params);

        return rooms.stream().map(RoomMapper.INSTANCE::toResponse).toList();
    }

    @Override
    public Room getRoomById(Long id) {
        Room r = roomRepo.getRoomById(id);
        if (r == null) {
            throw new RuntimeException("Room not found!");
        }
        return r;
    }

    @Override
    public Room saveOrUpdate(RoomForm form) {
        Room r;
        if (form.getId() == null) {
            r = new Room();
        } else {
            r = this.roomRepo.getRoomById(form.getId());
        }

        r.setRoomNumber(form.getRoomNumber());
        if (form.getArea() != null) {
            r.setAreaId(form.getArea());
        } else {
            throw new BadRequestException("Thiếu thông tin phân khu!");
        }

        return this.roomRepo.saveOrUpdate(r);
    }

    @Override
    public void deleteRoom(Long id) {
        this.roomRepo.deleteRoom(id);
    }

    @Override
    public long countRooms(Map<String, String> params) {
        return roomRepo.count(params, Room.class);
    }

}
