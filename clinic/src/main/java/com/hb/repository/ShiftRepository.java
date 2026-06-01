/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Shift;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface ShiftRepository extends BaseRepository<Shift>{
    List<Shift> getShifts(Map<String,String> params);
    Shift saveOrUpdate(Shift d);
    Shift getShiftById(Long id);
    void deleteShift(Long id);
    
}
