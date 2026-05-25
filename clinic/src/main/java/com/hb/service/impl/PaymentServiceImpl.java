/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.enums.PaymentStatus;
import com.hb.pojo.Appointment;
import com.hb.pojo.Patient;
import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItems;
import com.hb.repository.PaymentItemRepository;
import com.hb.repository.PaymentRepository;
import com.hb.service.PaymentItemsService;
import com.hb.service.PaymentService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
    private PaymentItemRepository itemRepo;

//    @Override
//    public List<Payment> getPayments(Map<String, String> params) {
//        return this.paymentRepo.getPayments(params);
//    }
//    @Override
//    public List<Payment> getPaymentsByUserName(Map<String, String> params) {
//        return this.paymentRepo.getPaymentsByUserName(params);
//    }
    @Override
    public Payment getPaymentById(Long id) {
        return this.paymentRepo.getPaymentById(id);
    }

    @Override
    public void deletePayment(Long id) {
        this.paymentRepo.deletePayment(id);
    }

    @Override
    public Payment createPayment(Long patientId) {
        Payment p = new Payment();

        p.setPatientId(new Patient(patientId));
        p.setStatus(PaymentStatus.PENDING);
        p.setCreatedAt(LocalDateTime.now());
        paymentRepo.addOrUpdatePayment(p);

        return p;
    }

    @Override
    public void updateStatusPayment(Long paymentId) {

        Payment p = paymentRepo.getPaymentById(paymentId);
        if (p != null) {

            List<PaymentItems> allItems = itemRepo.getItemsByPaymentId(paymentId);
            boolean isAllPaid = allItems.stream()
                    .allMatch(item -> PaymentStatus.SUCCESS.equals(item.getStatus()));

            if (isAllPaid) {
                p = paymentRepo.getPaymentById(paymentId);
                p.setStatus(PaymentStatus.SUCCESS);
                paymentRepo.addOrUpdatePayment(p);
            }
        }
    }

    @Override
    public Long calculateTotalFee(List<Long> itemIds) {

        long total = 0L;

        for (Long itemId : itemIds) {
            PaymentItems item = itemRepo.getItemById(itemId);

            if (item != null && item.getAmount() != null) {

                total += item.getAmount().longValue();
            }
        }

        return total;
    }

    @Override
    public Payment getPaymentByAppoint(Appointment appoint) {
        PaymentItems item = itemService.getPaymentItemByAppointment(appoint);
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

}
