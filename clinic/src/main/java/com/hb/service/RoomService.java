/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.form.RoomForm;
import com.hb.dto.response.RoomResponse;
import com.hb.pojo.Room;
import com.hb.pojo.User;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface RoomService {
    List<Room> getRooms(Map<String, String> params);  
    List<RoomResponse> getAvailableRoomsForDoctor(Map<String, String> params, User currentUser);
    Room getRoomById(Long id);
    Room saveOrUpdate(RoomForm form);
    void deleteRoom(Long id);
    long countRooms(Map<String, String> params);
}
