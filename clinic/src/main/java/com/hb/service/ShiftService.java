/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.form.ShiftForm;
import com.hb.dto.response.ShiftResponse;
import com.hb.pojo.Shift;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface ShiftService {
    List<ShiftResponse> getShifts(Map<String, String> params);
    Shift saveOrUpdate(ShiftForm  form);
    ShiftResponse getShiftById(Long id);
    void deleteShift(Long id);
    long countShifts(Map<String, String> params);
}
