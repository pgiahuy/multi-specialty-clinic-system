/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.response.ShiftResponse;
import com.hb.mapper.ShiftMapper;
import com.hb.pojo.Shifts;
import com.hb.repository.ShiftRepository;
import com.hb.service.ShiftService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */

@Service
public class ShiftServiceImpl implements ShiftService {
    
    @Autowired
    private ShiftRepository shiftRepo;

    @Override
    public List<ShiftResponse> getShifts(Map<String, String> params) {
        List<Shifts> res =  this.shiftRepo.getShifts(params);
        return res.stream().map(ShiftMapper.INSTANCE::toResponse).toList();
    }

    @Override
    public Shifts addShift(Map<String, String> params) {
        Shifts shift = new Shifts();
        return this.shiftRepo.addShift(shift);
    }

    @Override
    public ShiftResponse getShiftById(Long id) {
        Shifts s = this.shiftRepo.getShiftById(id);
        return ShiftMapper.INSTANCE.toResponse(s);
    }

    @Override
    public void deleteShift(Long id) {
        this.shiftRepo.deleteShift(id);
    }

    @Override
    public long countShifts(Map<String, String> params) {
        return shiftRepo.count(params, Shifts.class);
    }
    
}
