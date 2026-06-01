/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.LabResultCreateRequest;
import com.hb.dto.request.LabResultDetailRequest;
import com.hb.dto.response.LabResultResponse;
import com.hb.enums.LabResultStatus;
import com.hb.exception.ResourceNotFoundException;
import com.hb.mapper.LabResultMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.LabResult;
import com.hb.pojo.Payment;
import com.hb.repository.LabTestResultRepository;
import com.hb.service.AppointmentService;
import com.hb.service.LabResultDetailService;
import com.hb.service.LabTestService;
import com.hb.service.PaymentItemsService;
import com.hb.service.PaymentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hb.service.LabResultService;

/**
 *
 * @author DELL
 */
@Service
public class LabResultServiceImpl implements LabResultService {
    
    @Autowired
    private LabTestResultRepository labResultRepo;
    
    @Autowired
    private AppointmentService appointSer;
    
    @Autowired
    private LabTestService testService;
    
    @Autowired
    private LabResultMapper resultMapper;
    
    @Autowired
    private PaymentService payService;
    
    @Autowired
    private PaymentItemsService itemService;
    
    @Autowired
    private LabResultDetailService resultDetailService;
    
    @Override
    @Transactional
    public LabResult addOrUpdateLabResult(LabResultCreateRequest request) {
        LabResult labResult;
        
        if (request.getId() != null) {
            labResult = labResultRepo.getLabResultById(request.getId());
            labResult.setTestAt(request.getTestAt());
            labResult.setStatus(request.getStatus());
            resultDetailService.updateDetails(labResult.getId(), request.getDetails());
            labResultRepo.addOrUpdateTestResult(labResult);
            return labResult;
        }
        
        labResult = new LabResult();
        Appointment a = appointSer.getAppointmentById(request.getAppointmentId());

        if (a != null) {
            labResult.setAppointmentId(a);
            labResult.setCreatedAt(LocalDateTime.now());
            labResult.setStatus(LabResultStatus.PENDING);
        }
        
        labResultRepo.addOrUpdateTestResult(labResult);
        
        return labResult;
    }
    
    
    @Override
    public List<LabResultResponse> getLabResults(Map<String, String> params) {
        List<LabResult> res = labResultRepo.getLabResults(params);
        return res.stream().map(resultMapper::toResponse).toList();
    }
    
    @Override
    public List<LabResult> getLabResultsesByAppointmentId(Long appointmentId) {
        return this.labResultRepo.getLabResultsByAppointment(appointmentId);
    }
    
   
    @Override
    @Transactional
    public void labTestOrder(LabResultCreateRequest request) {
        LabResult labResult = this.addOrUpdateLabResult(request);
        List<LabResultDetailRequest> details = request.getDetails();

        if (labResult == null && details == null) {
            throw new ResourceNotFoundException("Failed to create lab result or no test details provided");
        }
        resultDetailService.addDetailsToLabResult(labResult.getId(), details);
        
        Payment payment = payService.createPayment(request.getAppointmentId());
        
        for (LabResultDetailRequest req : details) {
            itemService.addLabTestItems(payment, req.getTestId(), labResult.getId());
        }
        
        payService.updatePaymentTotalAmount(payment);
        
    }

    @Override
    public LabResult getLabResultById(Long id) {
        return labResultRepo.getLabResultById(id);
    }

    @Override
    public void updateStatusLabResult(Long labResultId, LabResultStatus status) {
        LabResult labResult = labResultRepo.getLabResultById(labResultId);
        if (labResult == null) {
            throw new ResourceNotFoundException("Không tìm thấy phiếu xét nghiệm!");
        }
        labResult.setStatus(status);
        labResultRepo.addOrUpdateTestResult(labResult);
    }
    
}
