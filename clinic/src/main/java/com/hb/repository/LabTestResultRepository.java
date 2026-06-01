/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.repository;

import com.hb.dto.response.LabResultResponse;
import com.hb.pojo.LabResult;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface LabTestResultRepository {
    LabResult getLabResultById(Long id);
    List<LabResult> getLabResultsByAppointment(Long appointmentId);
    void addOrUpdateTestResult(LabResult lr);
    List<LabResult> getLabResults(Map<String, String> params);
}
