/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItem;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface PaymentItemsService {
   
    void addAppointmentItem(Payment payment, Long appointmentId);
    
    void addLabTestItems(Payment payment, Long testId);
    
    void addPrescriptionItem(Payment payment, Long prescriptionId);
    
//    void confirmItemsPaid(String transId, String method, List<Long> itemIds);
    
    PaymentItem getPaymentItemByAppointment(Long  appointmentId);
    
    List<PaymentItem> getPaymentItemsByPaymentId(Long paymentId, Map<String, String> params);
}
