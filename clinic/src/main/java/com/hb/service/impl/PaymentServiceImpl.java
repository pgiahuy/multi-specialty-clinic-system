/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.enums.PaymentMethod;
import com.hb.enums.PaymentStatus;
import com.hb.pojo.Patient;
import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItems;
import com.hb.repository.AppointmentRepository;
import com.hb.repository.LabTestRepository;
import com.hb.repository.PaymentItemRepository;
import com.hb.repository.PaymentRepository;
import com.hb.repository.PrescriptionRepository;
import com.hb.service.PaymentItemsService;
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
    private LabTestRepository labRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private PaymentItemsService itemService;

    @Autowired
    private PaymentItemRepository itemRepo;

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

    @Override
    public Payment createPayment(Long patientId, Long appId, List<Long> testIds, Long presId) {
        Payment p = new Payment();
        p.setPatient(new Patient(patientId));
        p.setStatus(PaymentStatus.PENDING);
        p.setCreatedAt(new Date());
        paymentRepo.addOrUpdatePayment(p);

        // 2. Thêm các hạng mục thông qua ItemService (Tự động tính giá)
        if (appId != null) {
            itemService.addAppointmentItem(p, appId);
        }
        if (testIds != null) {
            itemService.addLabTestItems(p, testIds);
        }
        if (presId != null) {
            itemService.addPrescriptionItem(p, presId);
        }

        // 3. Tính tổng tiền từ các Item vừa tạo để cập nhật lại Payment
        List<PaymentItems> items = itemRepo.getItemsByPaymentId(p.getId());
        BigDecimal finalTotal = items.stream()
                .map(PaymentItems::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        p.setTotalAmount(finalTotal);
        paymentRepo.addOrUpdatePayment(p);

        return p;
    }

    @Override
    public void updateStatus(Long paymentId, PaymentStatus status) {
        Payment p = paymentRepo.getPaymentById(paymentId);
        if (p != null) {
            p.setStatus(status);
            paymentRepo.addOrUpdatePayment(p);
        }
    }

    @Override
    public void confirmPaymentSuccess(Long paymentId, String transId) {
        Payment p = paymentRepo.getPaymentById(paymentId);
        if (p != null) {
            p.setStatus(PaymentStatus.SUCCESS);
            p.setMethod(PaymentMethod.MOMO);

            // Lưu mã giao dịch từ MoMo vào DB
            p.setTransactionId(transId);

            paymentRepo.addOrUpdatePayment(p);
        }
    }

}
