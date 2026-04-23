/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.enums.PaymentMethod;
import com.hb.enums.PaymentStatus;
import com.hb.pojo.Payment;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface PaymentService {
//    List<Payment> getPayments(Map<String, String> params);
//    List<Payment> getPaymentsByUserName(Map<String, String> params);
    Payment getPaymentById(Long id);
    void deletePayment(Long id);
    Payment createPayment(Long patientId, Long appId, List<Long> testIds, Long presId);
    void updateStatus(Long paymentId, PaymentStatus status);
    
    Long calculateTotalFee(List<Long> itemIds);
    void confirmPaymentSuccess(Long paymentId, String transId, String method, List<Long> itemIds);
}