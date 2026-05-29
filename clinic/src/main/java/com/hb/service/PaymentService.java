/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.enums.PaymentMethod;
import com.hb.pojo.Appointment;
import com.hb.pojo.Payment;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface PaymentService {
    List<Payment> getPayments(Map<String, String> params);
    Payment getPaymentById(Long id);
    Payment getPaymentByAppoint(Appointment appoint);
    void deletePayment(Long id);
    Payment createPayment(Long appointmentId);
    void confirmPaymentSuccess(Long paymentId, PaymentMethod method);
    void confirmPaymentFailed(Long paymentId, PaymentMethod method);
    void updatePaymentTotalAmount(Payment payment);
    
    List<Payment> getPaymentByPatientId(Long patientId, Map<String, String> params);
    BigDecimal getPaymentAmount(Long paymentId);
}
