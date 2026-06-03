/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.enums.PaymentItemType;
import com.hb.pojo.Appointment;
import com.hb.pojo.LabTest;
import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItem;
import com.hb.pojo.Prescription;
import com.hb.pojo.PrescriptionItem;
import com.hb.exception.ResourceNotFoundException;
import com.hb.repository.AppointmentRepository;
import com.hb.repository.LabTestRepository;
import com.hb.repository.PaymentItemRepository;
import com.hb.repository.PrescriptionRepository;
import com.hb.service.PaymentItemsService;
import com.hb.service.PaymentService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DELL
 */
@Service
@Transactional
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
        if (app == null || app.getScheduleId() == null || app.getScheduleId().getSpecialtyId() == null) {
            throw new ResourceNotFoundException("Không tìm thấy chuyên khoa của lịch khám!");
        }

        BigDecimal price = app.getScheduleId().getSpecialtyId().getPrice();

        PaymentItem item = new PaymentItem();
        item.setPaymentId(payment);
        item.setItemType(PaymentItemType.APPOINTMENT);
        item.setAmount(price);
        item.setReferenceId(appointmentId);
        itemRepo.addOrUpdateItem(item);
        payService.updatePaymentTotalAmount(payment);

    }

    @Override
    public void addLabTestItems(Payment payment, Long testId, Long labResultId) {
        LabTest labtest = labRepo.getLabTestById(testId);
        PaymentItem item = new PaymentItem();
        item.setPaymentId(payment);
        item.setItemType(PaymentItemType.LAB_TEST);
        item.setAmount(labtest.getPrice());
        item.setReferenceId(labResultId);
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

        PaymentItem item = new PaymentItem();
        item.setPaymentId(payment);
        item.setItemType(PaymentItemType.PRESCRIPTION);
        item.setAmount(total);
        item.setReferenceId(prescriptionId);
        itemRepo.addOrUpdateItem(item);
        payService.updatePaymentTotalAmount(payment);
    }

    @Override

    public PaymentItem getPaymentItemByAppointment(Long  appointmentId) {

        return this.itemRepo.getItemByAppointment(appointmentId);
    }

    @Override
    public List<PaymentItem> getPaymentItemsByPaymentId(Long paymentId, Map<String, String> params) {
        return this.itemRepo.getItemsByPaymentId(paymentId, params);
    }

}
