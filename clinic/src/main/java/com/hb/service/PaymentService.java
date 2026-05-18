/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.Appointment;
import com.hb.pojo.Payment;
import java.util.List;

/**
 *
 * @author HUY
 */
public interface PaymentService {
//    List<Payment> getPayments(Map<String, String> params);
//    List<Payment> getPaymentsByUserName(Map<String, String> params);

    Payment getPaymentById(Long id);

    Payment getPaymentByAppoint(Appointment appoint);

    void deletePayment(Long id);

    Payment createPayment(Long patientId);

//    void updateStatus(Long paymentId, PaymentStatus status);

    Long calculateTotalFee(List<Long> itemIds);

    void updateStatusPayment(Long paymentId);

    void updatePaymentTotalAmount(Payment payment);
}
