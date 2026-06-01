/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.LabTestResultRequest;
import com.hb.dto.request.TestDetailRequest;
import com.hb.dto.response.LabTestResultResponse;
import com.hb.exception.ResourceNotFoundException;
import com.hb.mapper.LabTestResultMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.LabResultDetails;
import com.hb.pojo.LabResults;
import com.hb.pojo.LabTests;
import com.hb.pojo.Payment;
import com.hb.repository.LabResultDetailRepository;
import com.hb.repository.LabTestResultRepository;
import com.hb.service.AppointmentService;
import com.hb.service.LabTestResultService;
import com.hb.service.LabTestService;
import com.hb.service.PaymentItemsService;
import com.hb.service.PaymentService;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public LabResults addOrUpdateTestResult(LabTestResultRequest request) {
        LabResults labResult;
        
        if (request.getId() != null) {
            labResult = labResultRepo.getLabResultById(request.getId());
            labResultRepo.addOrUpdateTestResult(labResult);
            return labResult;
        }
        
        labResult = new LabResults();
        Appointment a = appointSer.getAppointmentById(request.getAppointId());
        if (a != null) {
            labResult.setAppointmentId(a);
            labResult.setCreatedAt(LocalDateTime.now());
            
        }
        labResultRepo.addOrUpdateTestResult(labResult);
        
        return labResult;
        
    }
    
    @Override
    @Transactional
    public void addDetailsToTestResult(Long labResultId, List<TestDetailRequest> requests) {
        
        LabResults labResult = labResultRepo.getLabResultById(requests.get(0).getLabResultId());
        
        for (TestDetailRequest req : requests) {
            LabResultDetails detail = new LabResultDetails();
            detail.setLabResultsId(labResult);
            LabTests labtest = testService.getLabTestById(req.getTestId());
            detail.setTestId(labtest);
            labResultDetailRepo.addOrUpdate(detail);            
        }
        
    }
    
    @Override
    public List<LabTestResultResponse> getTestResults(Long patientId, Map<String, String> params) {
        List<LabResults> res = labResultRepo.getTestResults(patientId, params);
        return res.stream().map(resultMapper::toResponse).toList();
    }
    
    @Override
    public List<LabResults> getLabResultsesByAppointmentId(Long appointmentId) {
        return this.labResultRepo.getLabResultsByAppointment(appointmentId);
    }
    
    @Override
    public List<LabResults> addMutipleTest(List<LabTestResultRequest> req) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public void labTestOrder(LabTestResultRequest request) {
        LabResults labResult = this.addOrUpdateTestResult(request);
        List<TestDetailRequest> details = request.getDetails();
        
        this.addDetailsToTestResult(labResult.getId(), details);
        
        Payment payment = payService.createPayment(request.getAppointId());
        
        for (TestDetailRequest req : details) {
            itemService.addLabTestItems(payment, req.getTestId());
        }
        
        payService.updatePaymentTotalAmount(payment);
        
    }
    
}
