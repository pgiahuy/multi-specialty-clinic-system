/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.request.LabTestResultRequest;
import com.hb.dto.response.LabTestResultResponse;
import com.hb.pojo.Appointment;
import com.hb.pojo.LabResults;
import com.hb.pojo.LabTests;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 *
 * @author DELL
 */
@Component
public class LabTestResultMapper {
    public LabResults toEntity(LabTestResultRequest req, Appointment appointment, LabTests labTest) {
        if (req == null) {
            return null;
        }
        LabResults labResult = new LabResults();
        
        labResult.setAppointmentId(appointment);
        labResult.setCreatedAt(LocalDateTime.now());
        labResult.setTestId(labTest);
        
        return labResult;
    }
    
    public LabTestResultResponse toResponse(LabResults labResult) {
        if (labResult == null) 
            return null;
        LabTestResultResponse res = new LabTestResultResponse();
        
        res.setId(labResult.getId());
        res.setResult(labResult.getResultValue());
        res.setIsNormal(labResult.getIsAbnormal());
        res.setCreateAt(labResult.getCreatedAt());
        
        if (labResult.getTestId() != null) {
            res.setTestName(labResult.getTestId().getTestName());
            res.setUnit(labResult.getTestId().getUnit());
        }
        
        if (labResult.getAppointmentId() != null) {
            res.setPatientName(labResult.getAppointmentId().getPatientId().getFullName());
        }
        
        return res;
    } 
}
