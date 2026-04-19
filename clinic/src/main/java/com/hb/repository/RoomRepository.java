/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Rooms;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface RoomRepository {
    List<Rooms> getRooms(Map<String,String> params);
    Rooms getRoomById(Long id);
    Rooms addRoom(Rooms a);
    void deleteRoom(Long id);
}
