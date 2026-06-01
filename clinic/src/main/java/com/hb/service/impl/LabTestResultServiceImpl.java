/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.LabResultCreateRequest;
import com.hb.dto.request.LabResultDetailRequest;
import com.hb.dto.response.LabTestResultResponse;
import com.hb.enums.LabResultStatus;
import com.hb.exception.ResourceNotFoundException;
import com.hb.mapper.LabTestResultMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.LabResult;
import com.hb.pojo.LabResultDetail;
import com.hb.pojo.LabTest;
import com.hb.pojo.Payment;
import com.hb.repository.LabResultDetailRepository;
import com.hb.repository.LabTestResultRepository;
import com.hb.service.AppointmentService;
import com.hb.service.LabTestResultService;
import com.hb.service.LabTestService;
import com.hb.service.PaymentItemsService;
import com.hb.service.PaymentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DELL
 */
@Service
public class LabTestResultServiceImpl implements LabTestResultService {
    
    @Autowired
    private LabTestResultRepository labResultRepo;
    
    @Autowired
    private AppointmentService appointSer;
    
    @Autowired
    private LabTestService testService;
    
    @Autowired
    private LabTestResultMapper resultMapper;
    
    @Autowired
    private PaymentService payService;
    
    @Autowired
    private PaymentItemsService itemService;
    
    @Autowired
    private LabResultDetailRepository labResultDetailRepo;
    
    @Override
    @Transactional
    public LabResult addOrUpdateTestResult(LabResultCreateRequest request) {
        LabResult labResult;
        
        if (request.getId() != null) {
            labResult = labResultRepo.getLabResultById(request.getId());
            labResultRepo.addOrUpdateTestResult(labResult);
            return labResult;
        }
        
        labResult = new LabResult();
        Appointment a = appointSer.getAppointmentById(request.getAppointId());

        if (a != null) {
            labResult.setAppointmentId(a);
            labResult.setCreatedAt(LocalDateTime.now());
            labResult.setStatus(LabResultStatus.PENDING);

        }
        labResultRepo.addOrUpdateTestResult(labResult);
        
        return labResult;
        
    }
    
    @Override
    @Transactional
    public void addDetailsToTestResult(Long labResultId, List<LabResultDetailRequest> requests) {
        
        LabResult labResult = labResultRepo.getLabResultById(labResultId);
        
        for (LabResultDetailRequest req : requests) {
            LabResultDetail detail = new LabResultDetail();
            detail.setLabResultsId(labResult);
            LabTest labtest = testService.getLabTestById(req.getTestId());
            detail.setTestId(labtest);
            labResultDetailRepo.addOrUpdate(detail);            

        }
        
    }
    
    @Override
    public List<LabTestResultResponse> getTestResults(Long patientId, Map<String, String> params) {
        List<LabResult> res = labResultRepo.getTestResults(patientId, params);
        return res.stream().map(resultMapper::toResponse).toList();
    }
    
    @Override
    public List<LabResult> getLabResultsesByAppointmentId(Long appointmentId) {
        return this.labResultRepo.getLabResultsByAppointment(appointmentId);
    }
    
    @Override
    public List<LabResult> addMutipleTest(List<LabResultCreateRequest> req) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    @Transactional
    public void labTestOrder(LabResultCreateRequest request) {
        LabResult labResult = this.addOrUpdateTestResult(request);
        List<LabResultDetailRequest> details = request.getDetails();

        if (labResult == null && details == null) {
            throw new ResourceNotFoundException("Failed to create lab result or no test details provided");
        }
        this.addDetailsToTestResult(labResult.getId(), details);
        Payment payment = payService.createPayment(request.getAppointId());
        
        for (LabResultDetailRequest req : details) {
            itemService.addLabTestItems(payment, req.getTestId());
        }
        
        payService.updatePaymentTotalAmount(payment);
        
    }
    
}
