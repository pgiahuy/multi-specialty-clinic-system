/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.enums.PaymentStatus;
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
import com.hb.service.PaymentItemsService;
import java.math.BigDecimal;
import java.util.List;
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

    @Override
    public void addAppointmentItem(Payment payment, Long appointmentId) {
        Appointment app = appRepo.getAppointmentById(appointmentId);

        BigDecimal price = app.getDoctor().getIdSpecailty().getPrice();

        PaymentItems item = new PaymentItems();
        item.setPaymentId(payment);
        item.setItemType("APPOINTMENT");
        item.setAmount(price);
        item.setAppointment(app);
        item.setStatus(PaymentStatus.PENDING);
        itemRepo.addOrUpdateItem(item);
    }

    @Override
    public void addLabTestItems(Payment payment, List<Long> testIds) {
        for (Long id : testIds) {
            LabTests lt = labRepo.getLabTestById(id);
            PaymentItems item = new PaymentItems();
            item.setPaymentId(payment);
            item.setItemType("TEST");
            item.setAmount(lt.getPrice());
            item.setLabTest(lt);
            item.setStatus(PaymentStatus.PENDING);
            itemRepo.addOrUpdateItem(item);
        }
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
        item.setPaymentId(payment);
        item.setItemType("PRESCRIPTION");
        item.setAmount(total);
        item.setPrescriptionId(pres);
        item.setStatus(PaymentStatus.PENDING);
        itemRepo.addOrUpdateItem(item);
    }
}
