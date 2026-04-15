/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Payment;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface PaymentRepository {
    List<Payment> getPaymentsByUserName(Map<String,String> params);
    List<Payment> getPayments(Map<String,String> params);
    Payment addPayment(Payment p);
    Payment getPaymentById(Long id);
    void deletePayment(Long id);
}
