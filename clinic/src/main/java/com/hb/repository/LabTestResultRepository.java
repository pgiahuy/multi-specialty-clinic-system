/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.repository;

import com.hb.dto.response.LabTestResultResponse;
import com.hb.pojo.LabResults;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface LabTestResultRepository {
    LabResults getLabResultById(Long id);
    void addOrUpdateTestResult(LabResults lr);
    List<LabResults> getTestResults(Long patientId, Map<String, String> params);
}
