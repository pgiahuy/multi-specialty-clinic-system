/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.response.PaymentResponse;
import com.hb.enums.PaymentMethod;
import com.hb.enums.PaymentStatus;
import com.hb.enums.AppointmentStatus;
import com.hb.enums.LabResultStatus;
import com.hb.enums.PaymentItemType;
import com.hb.exception.ResourceNotFoundException;
import com.hb.mapper.PaymentMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.LabResult;
import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItem;
import com.hb.pojo.User;
import com.hb.repository.PaymentItemRepository;
import com.hb.repository.PaymentRepository;
import com.hb.repository.AppointmentRepository;
import com.hb.service.AppointmentService;
import com.hb.service.LabResultService;
import com.hb.service.NotificationService;
import com.hb.service.PaymentItemsService;
import com.hb.service.PaymentService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HUY
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private PaymentItemsService itemService;

    @Autowired
    private AppointmentService appointService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PaymentItemRepository itemRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private LabResultService labResultService;
    
    @Autowired
    private PaymentMapper payMapper;
    
    @Override
    @Transactional
    public List<PaymentResponse> getPayments(Map<String, String> params) {
        
        
        List<Payment> payments = this.paymentRepo.getPayments(params);
        
        
        return payments.stream()
                .map(payMapper::toResponse)
                .toList(); 
    } 

    @Override
    @Transactional
    public List<PaymentResponse> getPaymentsByUserName(Map<String, String> params) {
        List<Payment> payments = this.paymentRepo.getPaymentsByUserName(params);
        return payments.stream()
                .map(payMapper::toResponse)
                .toList();
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        return payMapper.toResponse(this.paymentRepo.getPaymentById(id));
    }

    @Override
    public void deletePayment(Long id) {
        this.paymentRepo.deletePayment(id);
    }

    @Override
    public Payment createPayment(Long appointmentId) {
        Payment p = new Payment();
        Appointment a = appointService.getAppointmentById(appointmentId);
        p.setAppointmentId(a);
        p.setStatus(PaymentStatus.PENDING);
        p.setTotalAmount(BigDecimal.ONE);
        p.setCreatedAt(LocalDateTime.now());
        paymentRepo.addOrUpdatePayment(p);

        return p;
    }

    @Override
    @Transactional
    public Payment getPaymentByAppoint(Long appointmentId) {
        PaymentItem item = itemService.getPaymentItemByAppointment(appointmentId);
        if (item == null || item.getPaymentId() == null) {
            return null;
        }

        Payment payment = this.paymentRepo.getPaymentById(item.getPaymentId().getId());
        if (payment != null && payment.getPaymentItemCollection() != null) {
            payment.getPaymentItemCollection().size();
        }
        return payment;
    }

    @Override
    public void updatePaymentTotalAmount(Payment payment) {
        List<PaymentItem> items = itemRepo.getItemsByPayment(payment);

        BigDecimal total = BigDecimal.ZERO;
        if (items != null && !items.isEmpty()) {
            for (PaymentItem item : items) {
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
    @Transactional
    public void confirmPaymentSuccess(Long paymentId, PaymentMethod method) {
        Payment payment = paymentRepo.getPaymentById(paymentId);
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setMethod(method);
        payment.setPaidAt(LocalDateTime.now());

        Payment saved = paymentRepo.addOrUpdatePayment(payment);

        Collection<PaymentItem> items = payment.getPaymentItemCollection();

        PaymentItem item = items.stream().findFirst().orElse(null);

        PaymentItemType type = item.getItemType();

        switch (type) {
            case APPOINTMENT -> {
                Appointment appointment = appointService.getAppointmentById(item.getReferenceId());
                this.confirmPaymentForAppointment(appointment.getId());

            }

            case LAB_TEST -> {
                LabResult labResult = labResultService.getLabResultEntityById(item.getReferenceId());
                this.confirmPaymentForLabResult(labResult.getId());
            }

        }

        try {
            if (saved != null && saved.getAppointmentId().getPatientId().getUserId() != null) {

                User patientUser = saved.getAppointmentId().getPatientId().getUserId();
                if (patientUser != null) {
                    Map<String, String> notiParams = new HashMap<>();
                    notiParams.put("username", patientUser.getUsername());
                    notiParams.put("title", "Thanh toán thành công!");
                    notiParams.put("content", "Bạn vừa thanh toán thành công một hoá đơn. Vui lòng kiểm tra!");
                    notiParams.put("path", "/patient/payments");
                    this.notificationService.addNotification(notiParams);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi gửi thông báo: " + e.getMessage());
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

    @Override
    public void confirmPaymentForAppointment(Long appointmentId) {
        appointService.updateStatusAppointment(appointmentId, AppointmentStatus.PENDING);
    }

    @Override
    public void confirmPaymentForLabResult(Long labResultId) {
        labResultService.updateStatusLabResult(labResultId, LabResultStatus.CONFIRMED);
    }

    @Override
    public void confirmPaymentForPrescription(Long prescriptionId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Payment getPaymentEntityById(Long id) {
        return this.paymentRepo.getPaymentById(id);
    }

}
