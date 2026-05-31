/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.enums.PaymentMethod;
import com.hb.enums.PaymentStatus;
import com.hb.enums.AppointmentStatus;
import com.hb.pojo.Appointment;
import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItems;
import com.hb.repository.PaymentItemRepository;
import com.hb.repository.PaymentRepository;
import com.hb.repository.AppointmentRepository;
import com.hb.service.AppointmentService;
import com.hb.service.PaymentItemsService;
import com.hb.service.PaymentService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private PaymentItemsService itemService;
    
    @Autowired
    private AppointmentService appointService;

    @Autowired
    private PaymentItemRepository itemRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Override
    public List<Payment> getPayments(Map<String, String> params) {
        return this.paymentRepo.getPayments(params);
    }

    
    @Override
    public Payment getPaymentById(Long id) {
        return this.paymentRepo.getPaymentById(id);
    }

    @Override
    public void deletePayment(Long id) {
        this.paymentRepo.deletePayment(id);
    }

    @Override
    public Payment createPayment(Appointment appointment) {
        Payment p = new Payment();

        p.setAppointmentId(appointment);
        p.setStatus(PaymentStatus.PENDING);
        p.setTotalAmount(BigDecimal.ONE);
        p.setCreatedAt(LocalDateTime.now());
        paymentRepo.addOrUpdatePayment(p);

        return p;
    }


    @Override
    public Payment getPaymentByAppoint(Long appointmentId) {
        PaymentItems item = itemService.getPaymentItemByAppointment(appointmentId);
        return this.paymentRepo.getPaymentById(item.getPaymentId().getId());
    }

    @Override
    public void updatePaymentTotalAmount(Payment payment) {
        List<PaymentItems> items = itemRepo.getItemsByPayment(payment);

        BigDecimal total = BigDecimal.ZERO;
        if (items != null && !items.isEmpty()) {
            for (PaymentItems item : items) {
                if (item.getAmount() != null) {
                    total = total.add(item.getAmount());
                }
            }
        }

        payment.setTotalAmount(total);
        paymentRepo.addOrUpdatePayment(payment);
    }

    @Override
    public List<Payment> getPaymentByPatientId(Long patientId, Map<String, String> params) {
        return this.paymentRepo.getPaymentByPatientId(patientId, params);
    }
    
    
    @Override
    public BigDecimal getPaymentAmount(Long paymentId) {
        Payment p = paymentRepo.getPaymentById(paymentId);
        return p.getTotalAmount();
    }

    @Override
    public void confirmPaymentSuccess(Long paymentId, PaymentMethod method) {
        Payment p = paymentRepo.getPaymentById(paymentId);
        if (p.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }
        
        p.setStatus(PaymentStatus.SUCCESS);
        p.setMethod(method);
        p.setPaidAt(LocalDateTime.now());
        
        paymentRepo.addOrUpdatePayment(p);

        if (p.getAppointmentId() != null) {
            p.getAppointmentId().setStatus(AppointmentStatus.PENDING);
            appointmentRepo.addOrUpdateAppointment(p.getAppointmentId());
        }
    }

    @Override
    public void confirmPaymentFailed(Long paymentId, PaymentMethod method) {
        Payment p = paymentRepo.getPaymentById(paymentId);
        if (p.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }
        
        p.setStatus(PaymentStatus.FAILURE);
        p.setMethod(method);
        p.setPaidAt(LocalDateTime.now());
        
        paymentRepo.addOrUpdatePayment(p);
    }

}
