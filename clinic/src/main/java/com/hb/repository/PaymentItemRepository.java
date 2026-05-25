/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Appointment;
import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItems;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface PaymentItemRepository {
    List<PaymentItems> getItemsByPaymentId(Long paymentId);
    List<PaymentItems> getItemsByPayment(Payment payment);
    List<PaymentItems> getPaymentItems(Map<String,String> params);
    PaymentItems getItemById(Long id);
    PaymentItems getItemByAppointment(Appointment appoint);
    void addOrUpdateItem(PaymentItems item);
    void deleteItem(Long id);
}
