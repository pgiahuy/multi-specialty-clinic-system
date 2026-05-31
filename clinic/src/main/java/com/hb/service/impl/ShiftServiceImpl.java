/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.form.ShiftForm;
import com.hb.dto.response.ShiftResponse;
import com.hb.mapper.ShiftMapper;
import com.hb.pojo.Shift;
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
        List<Shift> res = this.shiftRepo.getShifts(params);
        return res.stream().map(ShiftMapper.INSTANCE::toResponse).toList();
    }

    @Override
    public Shift saveOrUpdate(ShiftForm form) {
        Shift s;
        if (form.getId() == null) {
            s = new Shift();
        } else {
            s = this.shiftRepo.getShiftById(form.getId());
        }

        s.setStartTime(form.getStartTime() != null ? form.getStartTime() : null);
        s.setEndTime(form.getEndTime() != null ? form.getEndTime() : null);
        s.setSession(form.getSession()!= null ? form.getSession() : null);
        s.setMinPatients(form.getMinPatients()!= null ? form.getMinPatients() : null);
        s.setMaxPatients(form.getMaxPatients()!= null ? form.getMaxPatients() : null);
        return this.shiftRepo.saveOrUpdate(s);
    }

    @Override
    public ShiftResponse getShiftById(Long id) {
        Shift s = this.shiftRepo.getShiftById(id);
        return ShiftMapper.INSTANCE.toResponse(s);
    }

    @Override
    public void deleteShift(Long id) {
        this.shiftRepo.deleteShift(id);
    }

    @Override
    public long countShifts(Map<String, String> params) {
        return shiftRepo.count(params, Shift.class);
    }

}
