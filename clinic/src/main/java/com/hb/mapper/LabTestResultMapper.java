/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.request.LabResultCreateRequest;
import com.hb.dto.response.LabTestResultResponse;
import com.hb.pojo.Appointment;
import com.hb.pojo.LabResult;
import com.hb.pojo.LabTest;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 *
 * @author DELL
 */
@Component
public class LabTestResultMapper {
    public LabResult toEntity(LabResultCreateRequest req, Appointment appointment, LabTest labTest) {
        if (req == null) {
            return null;
        }
        LabResult labResult = new LabResult();
        
        labResult.setAppointmentId(appointment);
        labResult.setCreatedAt(LocalDateTime.now());
        return labResult;
    }
    
    public LabTestResultResponse toResponse(LabResult labResult) {
        if (labResult == null) 
            return null;
        LabTestResultResponse res = new LabTestResultResponse();
        
        res.setId(labResult.getId());
       
        res.setCreateAt(labResult.getCreatedAt());
        
       
        
        if (labResult.getAppointmentId() != null) {
            res.setPatientName(labResult.getAppointmentId().getPatientId().getFullName());
        }
        
        return res;
    } 
}
