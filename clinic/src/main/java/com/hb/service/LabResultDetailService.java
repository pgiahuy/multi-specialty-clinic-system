/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.LabResultDetailRequest;
import com.hb.pojo.LabResultDetail;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface LabResultDetailService {
    LabResultDetail getLabResultDetailById(Long id);
    void addDetailsToLabResult(Long labResultId, List<LabResultDetailRequest> reqs);
    void updateDetails(Long labResultId, List<LabResultDetailRequest> reqs);
}
