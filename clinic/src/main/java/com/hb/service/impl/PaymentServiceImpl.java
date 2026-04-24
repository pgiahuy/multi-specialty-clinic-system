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
import com.hb.repository.PaymentItemRepository;
import com.hb.repository.PaymentRepository;
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
    public Payment createPayment(Long patientId, Long appId, List<Long> testIds, Long presId) {
        Payment p = new Payment();
        p.setPatientId(new Patient(patientId));
        p.setStatus(PaymentStatus.PENDING);
        p.setCreatedAt(new Date());
        paymentRepo.addOrUpdatePayment(p);

        if (appId != null) {
            itemService.addAppointmentItem(p, appId);
        }

        if (testIds != null) {
            itemService.addLabTestItems(p, testIds);
        }

        if (presId != null) {
            itemService.addPrescriptionItem(p, presId);
        }

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
    public void confirmPaymentSuccess(Long paymentId, String transId, String method, List<Long> itemIds) {
        itemService.confirmItemsPaid(transId, method, itemIds);

        List<PaymentItems> allItems = itemRepo.getItemsByPaymentId(paymentId);
        boolean isAllPaid = allItems.stream()
                .allMatch(item -> PaymentStatus.SUCCESS.equals(item.getStatus()));

        if (isAllPaid) {
            Payment p = paymentRepo.getPaymentById(paymentId);
            p.setStatus(PaymentStatus.SUCCESS);
            paymentRepo.addOrUpdatePayment(p);
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

}
