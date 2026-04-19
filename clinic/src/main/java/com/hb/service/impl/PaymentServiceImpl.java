/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.enums.PaymentMethod;
import com.hb.enums.PaymentStatus;
import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.Appointment;
import com.hb.pojo.Payment;
import com.hb.repository.AppointmentRepository;
import com.hb.repository.PaymentRepository;
import com.hb.service.PaymentService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;
    
    @Autowired
    private AppointmentRepository appointmentRepo;

    @Override
    public Payment addPayment(Map<String, String> params) {
        return null;
//        Payment p = new Payment();
//        
//
//        String amountStr = params.get("amount");
//        if (amountStr != null && !amountStr.isEmpty()) {
//            try {
//                BigDecimal amount = new BigDecimal(amountStr);
//                p.setAmount(amount);
//            } catch (NumberFormatException e) {
//                throw new RuntimeException("Invalid amount format");
//            }
//        } else {
//            throw new ResourceNotFoundException("Amount is required");
//        }
//
//        p.setMethod(params.getOrDefault("method", PaymentMethod.CASH.name()));
//        p.setStatus(params.getOrDefault("status", PaymentStatus.PENDING.name()));
//        p.setCreatedAt(new Date());
//
//        String appointmentIdStr = params.get("appointmentId");
//        if (appointmentIdStr != null && !appointmentIdStr.isEmpty()) {
//            try {
//                Long appointmentId = Long.parseLong(appointmentIdStr);
//                Appointment appointment = appointmentRepo.getAppointmentById(appointmentId);
//                p.setAppointmentId(appointment);
//                
//            } catch (NumberFormatException e) {
//                throw new RuntimeException("Invalid appointment ID format");
//            }
//        }
//
//        return this.paymentRepo.addPayment(p);
    }

    @Override
    public List<Payment> getPayments(Map<String, String> params) {
        return this.paymentRepo.getPayments(params);
    }

    @Override
    public List<Payment> getPaymentsByUserName(Map<String, String> params) {
        return this.paymentRepo.getPaymentsByUserName(params);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return this.paymentRepo.getPaymentById(id);
    }

    @Override
    public void deletePayment(Long id) {
        this.paymentRepo.deletePayment(id);
    }
}