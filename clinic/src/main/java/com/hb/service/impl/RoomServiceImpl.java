/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.form.RoomForm;
import com.hb.exception.BadRequestException;
import com.hb.pojo.Area;
import com.hb.pojo.Room;
import com.hb.repository.RoomRepository;
import com.hb.service.RoomService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hb.repository.AreaRepository;

/**
 *
 * @author DELL
 */
@Service
public class RoomServiceImpl implements RoomService{
    @Autowired
    private RoomRepository roomRepo;
    
    @Autowired
    private AreaRepository areaRepo;
    
    @Override
    public List<Room> getRooms(Map<String, String> params) {
        return roomRepo.getRooms(params);
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
        if (form.getId()==null) {
            r = new Room();
        }else{
            r = this.roomRepo.getRoomById(form.getId());
        }
        
        r.setRoomNumber(form.getRoomNumber());
        if(form.getArea()!=null){
            r.setAreaId(form.getArea());
        }else{
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
