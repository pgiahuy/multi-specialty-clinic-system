/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.LabResultCreateRequest;
import com.hb.dto.request.LabResultDetailRequest;
import com.hb.dto.response.LabResultResponse;
import com.hb.enums.LabResultStatus;
import com.hb.pojo.LabResult;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */

public interface LabResultService {
    LabResult addOrUpdateLabResult(LabResultCreateRequest request);
    List<LabResultResponse> getLabResults(Map<String, String> params);
    LabResult getLabResultById(Long id);
    List<LabResult> getLabResultsesByAppointmentId(Long appointmentId);
    void labTestOrder(LabResultCreateRequest request);
    void updateStatusLabResult(Long labResultId, LabResultStatus status);

}
