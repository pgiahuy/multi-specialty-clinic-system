/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Shifts;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface ShiftRepository extends BaseRepository<Shifts>{
    List<Shifts> getShifts(Map<String,String> params);
    Shifts addShift(Shifts d);
    Shifts getShiftById(Long id);
    void deleteShift(Long id);
    
}
