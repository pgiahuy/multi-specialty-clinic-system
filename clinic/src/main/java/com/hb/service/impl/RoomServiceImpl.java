/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.pojo.Rooms;
import com.hb.repository.RoomRepository;
import com.hb.service.RoomService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class RoomServiceImpl implements RoomService{
    @Autowired
    private RoomRepository roomRepo;
    
    @Override
    public List<Rooms> getRooms(Map<String, String> params) {
        return roomRepo.getRooms(params);
    }

    @Override
    public Rooms getRoomById(Long id) {
        Rooms r = roomRepo.getRoomById(id);
        if (r == null) {
            throw new RuntimeException("Room not found!");
        }
        return r;
    }

    @Override
    public Rooms addRoom(Map<String, String> params) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteRoom(Long id) {
         this.roomRepo.deleteRoom(id); 
    }

    @Override
    public long countRooms(Map<String, String> params) {
        return roomRepo.count(params, Rooms.class);
    }
    
}
