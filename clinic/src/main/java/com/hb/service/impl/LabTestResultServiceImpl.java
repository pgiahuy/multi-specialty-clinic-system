/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.LabTestResultRequest;
import com.hb.dto.response.LabTestResultResponse;
import com.hb.mapper.LabTestResultMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.LabResults;
import com.hb.pojo.LabTests;
import com.hb.pojo.Payment;
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
  

    @Override
    public LabResults addOrUpdateTestResult(LabTestResultRequest request) {
        LabResults labResult;
        
        if(request.getId() != null) {
            labResult = labResultRepo.getLabResultById(request.getId());
            labResult.setResultValue(request.getResult());
            labResult.setIsAbnormal(request.getIsNormal());
            labResultRepo.addOrUpdateTestResult(labResult);
            return labResult;
        }
        
        labResult = new LabResults();
        Appointment a = appointSer.getAppointmentById(request.getAppointId());
        LabTests test = testService.getLabTestById(request.getTestId());
        if (a != null && test != null) {
            labResult = resultMapper.toEntity(request, a, test);
            labResultRepo.addOrUpdateTestResult(labResult);
        }
       return labResult;
    }

    @Override
    @Transactional
    public List<LabResults> addMutipleTest(List<LabTestResultRequest> reqs) {   
        List<LabResults> savedResults = new ArrayList<>();
        Appointment a = appointSer.getAppointmentById(reqs.get(0).getAppointId());
        Payment payment = payService.createPayment(a);
        
       for (LabTestResultRequest req : reqs) {
           
            LabResults r = this.addOrUpdateTestResult(req);
            savedResults.add(r);
            
           
            if (req.getId() == null) {
                LabTests test = testService.getLabTestById(req.getTestId());
                itemService.addLabTestItems(payment, test.getId());
            }
        }
        
        return savedResults;
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

    
    
}
