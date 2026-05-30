/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.enums.AppointmentStatus;
import com.hb.enums.PaymentItemType;
import com.hb.enums.PaymentMethod;
import com.hb.enums.PaymentStatus;
import com.hb.mapper.AppointmentMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.LabTests;
import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItems;
import com.hb.pojo.Prescription;
import com.hb.pojo.PrescriptionItem;
import com.hb.repository.AppointmentRepository;
import com.hb.repository.LabTestRepository;
import com.hb.repository.PaymentItemRepository;
import com.hb.repository.PrescriptionRepository;
import com.hb.service.AppointmentService;
import com.hb.service.PaymentItemsService;
import com.hb.service.PaymentService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class PaymentItemsServiceImpl implements PaymentItemsService {

    @Autowired
    private PaymentItemRepository itemRepo;
    
    @Autowired
    private LabTestRepository labRepo;
    
    @Autowired
    private AppointmentRepository appRepo;
    
    @Autowired
    private PrescriptionRepository presRepo;

    @Autowired
    private PaymentService payService;

    @Override
    public void addAppointmentItem(Payment payment, Long appointmentId) {
        Appointment app = appRepo.getAppointmentById(appointmentId);

        BigDecimal price = app.getScheduleId().getSpecialtyId().getPrice();

        PaymentItems item = new PaymentItems();
        item.setPayment(payment);
        item.setItemType(PaymentItemType.APPOINTMENT);
        item.setAmount(price);
        item.setReferenceId(appointmentId);
        itemRepo.addOrUpdateItem(item);
        payService.updatePaymentTotalAmount(payment);
        
    }

    @Override
    public void addLabTestItems(Payment payment, Long testId) {

        LabTests lt = labRepo.getLabTestById(testId);
        PaymentItems item = new PaymentItems();
        item.setPayment(payment);
        item.setItemType(PaymentItemType.LAB_TEST);
        item.setAmount(lt.getPrice());
        item.setReferenceId(testId);
        itemRepo.addOrUpdateItem(item);
        payService.updatePaymentTotalAmount(payment);

    }

    @Override
    public void addPrescriptionItem(Payment payment, Long prescriptionId) {
        Prescription pres = presRepo.getPrescriptionById(prescriptionId);
        BigDecimal total = BigDecimal.ZERO;

        for (PrescriptionItem pi : pres.getPrescriptionItemCollection()) {
            BigDecimal price = pi.getMedicineId().getPrice();
            BigDecimal qty = new BigDecimal(pi.getQuantity());
            total = total.add(price.multiply(qty));
        }

        PaymentItems item = new PaymentItems();
        item.setPayment(payment);
        item.setItemType(PaymentItemType.PRESCRIPTION);
        item.setAmount(total);
        item.setReferenceId(prescriptionId);
        itemRepo.addOrUpdateItem(item);
        payService.updatePaymentTotalAmount(payment);
    }

//    @Override
//    public void confirmItemsPaid(String transId, String method, List<Long> itemIds) {
//        for (Long id : itemIds) {
//            PaymentItems item = itemRepo.getItemById(id);
//            if (item != null) {
//                if (item.getItemType().equals(PaymentItemType.APPOINTMENT)) {
//                    Appointment a = item.getAppointmentId();
//
//                    a.setStatus(AppointmentStatus.CONFIRMED);
//
//                    appRepo.addOrUpdateAppointment(a);
//                }
//                
//                item.setStatus(PaymentStatus.SUCCESS);
//                item.setMethod(PaymentMethod.valueOf(method));
//                item.setTransId(transId);
//                item.setPaidAt(LocalDateTime.now());
//                itemRepo.addOrUpdateItem(item);
//            }
//        }
//    }

    @Override
    public PaymentItems getPaymentItemByAppointment(Long  appointmentId) {
        return this.itemRepo.getItemByAppointment(appointmentId);
    }

    @Override
    public List<PaymentItems> getPaymentItemsByPaymentId(Long paymentId, Map<String, String> params) {
        return this.itemRepo.getItemsByPaymentId(paymentId, params);
    }

}
