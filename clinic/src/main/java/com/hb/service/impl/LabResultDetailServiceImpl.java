/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.LabResultDetailRequest;
import com.hb.exception.BadRequestException;
import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.LabResult;
import com.hb.pojo.LabResultDetail;
import com.hb.pojo.LabTest;
import com.hb.repository.LabResultDetailRepository;
import com.hb.service.LabResultDetailService;
import com.hb.service.LabResultService;
import com.hb.service.LabTestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DELL
 */
@Service
public class LabResultDetailServiceImpl implements LabResultDetailService{
    
    @Autowired
    private LabResultDetailRepository resultDetailRepo;
    
    @Autowired
    private LabTestService labTestService;
    
    @Autowired
    private LabResultService labResultService;

    @Override
    public LabResultDetail getLabResultDetailById(Long id) {
        return resultDetailRepo.getLabResultDetailById(id);
    }

    @Override
    @Transactional
    public void addDetailsToLabResult(Long labResultId, List<LabResultDetailRequest> reqs) {
        LabResult labResult = labResultService.getLabResultEntityById(labResultId);
        
        if (labResult == null) {
            throw new ResourceNotFoundException("Không tìm thấy phiếu xét nghiệm!");
        }
        
        for (LabResultDetailRequest req : reqs) {
            LabResultDetail detail = new LabResultDetail();
            detail.setLabResultsId(labResult);
            LabTest labtest = labTestService.getLabTestById(req.getTestId());
            detail.setTestId(labtest);
            resultDetailRepo.addOrUpdate(detail);            
        }
    }

    @Override
    @Transactional
    public void updateDetails(Long labResultId, List<LabResultDetailRequest> reqs) {
        LabResult labResult = labResultService.getLabResultEntityById(labResultId);
        
        if (labResult == null) {
            throw new ResourceNotFoundException("Không tìm thấy phiếu xét nghiệm!");
        }
        
        if (reqs == null) {
            throw new BadRequestException("Không có kết quả xét nghiệm nào");
        }
        
        for (LabResultDetailRequest req : reqs) {
            LabResultDetail resultDetail = resultDetailRepo.getLabResultDetailById(req.getId());
            resultDetail.setValue(req.getValue());
            resultDetail.setIsAbnormal(req.getIsAbnormal());
            resultDetailRepo.addOrUpdate(resultDetail);
        }
    }
    
}
