/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItems;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface PaymentItemsService {
   
    void addAppointmentItem(Payment payment, Long appointmentId);
    
    void addLabTestItems(Payment payment, List<Long> testIds);
    
    void addPrescriptionItem(Payment payment, Long prescriptionId);
}
