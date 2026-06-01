/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Room;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface RoomRepository extends BaseRepository<Room>{
    List<Room> getRooms(Map<String,String> params);
    List<Room> getAvailableRooms(Map<String, String> params);
    Room getRoomById(Long id);
    Room saveOrUpdate(Room a);
    void deleteRoom(Long id);
}
