/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Appointment;
import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItem;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface PaymentItemRepository {
    List<PaymentItem> getItemsByPaymentId(Long paymentId);
    List<PaymentItem> getItemsByPayment(Payment payment);
    List<PaymentItem> getPaymentItems(Map<String,String> params);
    PaymentItem getItemById(Long id);
    PaymentItem getItemByAppointment(Long appointmentId);
    void addOrUpdateItem(PaymentItem item);
    void deleteItem(Long id);
    List<PaymentItem> getItemsByPaymentId(Long paymentId, Map<String, String> params);
}
