/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.LabTestResultRequest;
import com.hb.dto.response.LabTestResultResponse;
import com.hb.pojo.LabResult;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */

public interface LabTestResultService {
    LabResult addOrUpdateTestResult(LabTestResultRequest request);
    List<LabResult> addMutipleTest(List<LabTestResultRequest> req);
    List<LabTestResultResponse> getTestResults(Long patientId, Map<String, String> params);
    List<LabResult> getLabResultsesByAppointmentId(Long appointmentId);
}
