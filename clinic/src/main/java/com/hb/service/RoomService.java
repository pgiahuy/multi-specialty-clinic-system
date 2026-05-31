/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.form.RoomForm;
import com.hb.pojo.Room;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface RoomService {

    List<Room> getRooms(Map<String, String> params);

    Room getRoomById(Long id);

    Room saveOrUpdate(RoomForm form);

    void deleteRoom(Long id);

    long countRooms(Map<String, String> params);
}
