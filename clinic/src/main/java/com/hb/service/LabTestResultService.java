/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.LabTestResultRequest;
import com.hb.dto.response.LabTestResultResponse;
import com.hb.pojo.LabResults;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */

public interface LabTestResultService {
    LabResults addOrUpdateTestResult(LabTestResultRequest request);
    List<LabResults> addMutipleTest(List<LabTestResultRequest> req);
    List<LabTestResultResponse> getTestResults(Long patientId, Map<String, String> params);
    List<LabResults> getLabResultsesByAppointmentId(Long appointmentId);
}
