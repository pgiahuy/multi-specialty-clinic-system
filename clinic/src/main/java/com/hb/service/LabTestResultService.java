/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.LabResultCreateRequest;
import com.hb.dto.request.LabResultDetailRequest;
import com.hb.dto.response.LabTestResultResponse;
import com.hb.pojo.LabResult;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */

public interface LabTestResultService {
    LabResult addOrUpdateTestResult(LabResultCreateRequest request);
    List<LabResult> addMutipleTest(List<LabResultCreateRequest> req);
    List<LabTestResultResponse> getTestResults(Long patientId, Map<String, String> params);

    List<LabResult> getLabResultsesByAppointmentId(Long appointmentId);
    void addDetailsToTestResult(Long labResultId, List<LabResultDetailRequest> request);
    void labTestOrder(LabResultCreateRequest request);

}
