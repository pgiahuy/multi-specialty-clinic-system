/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.Shifts;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface ShiftService {
    List<Shifts> getShifts(Map<String, String> params);
    Shifts addShift(Map<String, String> params);
    Shifts getShiftById(Long id);
    void deleteShift(Long id);
}
