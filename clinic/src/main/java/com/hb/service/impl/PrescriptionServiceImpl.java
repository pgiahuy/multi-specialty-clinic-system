/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.PrescriptionCreateRequest;
import com.hb.exception.BadRequestException;
import com.hb.exception.InsufficientStockException;
import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.MedicalRecord;
import com.hb.pojo.Medicine;
import com.hb.pojo.Prescription;
import com.hb.pojo.PrescriptionItem;
import com.hb.pojo.User;
import com.hb.repository.MedicalRecordRepository;
import com.hb.repository.MedicineRepository;
import com.hb.repository.PrescriptionItemRepository;
import com.hb.repository.PrescriptionRepository;
import com.hb.service.NotificationService;
import com.hb.service.PrescriptionService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HUY
 */
@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepo;
    
    @Autowired
    private PrescriptionItemRepository prescriptionItemRepo;

    @Autowired
    private NotificationService notificationService;


    @Autowired
    private MedicalRecordRepository medicalRecordRepo;

    @Autowired
    private MedicineRepository medicineRepo;
    
    @Override
    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptions(Map<String, String> params) {
        List<Prescription> prescriptions = this.prescriptionRepo.getPrescriptions(params);

        for (Prescription prescription : prescriptions) {
            if (prescription.getPrescriptionItemCollection() != null) {
                prescription.getPrescriptionItemCollection().size();

                for (PrescriptionItem item : prescription.getPrescriptionItemCollection()) {
                    if (item.getMedicineId() != null) {
                        item.getMedicineId().getName();
                        item.getMedicineId().getSecureUrl();
                    }
                }
            }
        }

        return prescriptions;
    }

    @Override
    @Transactional
    public Prescription addPrescription(PrescriptionCreateRequest req) {
        if (req == null) {
            throw new BadRequestException("Request body is required");
        }

        if (req.getMedicalRecordId() == null) {
            throw new BadRequestException("medicalRecordId is required");
        }

        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new BadRequestException("At least one prescription item is required");
        }

        Prescription p = new Prescription();
        p.setCreatedAt(new Date());

        MedicalRecord mr = medicalRecordRepo.getMedicalRecordById(req.getMedicalRecordId());
        if (mr == null) {
            throw new ResourceNotFoundException("Medical record not found!");
        }
        
        if (mr.getPrescription() != null) {
            throw new BadRequestException("Đã có đơn thuốc cho hồ sơ này!");
        }

        p.setMedicalRecordId(mr);

        List<PrescriptionItem> items = new ArrayList<>();

        for (var i : req.getItems()) {
            if (i == null) {
                throw new BadRequestException("Prescription item is required");
            }

            if (i.getMedicineId() == null) {
                throw new BadRequestException("medicineId is required");
            }

            if (i.getQuantity() <= 0) {
                throw new BadRequestException("quantity > 0");
            }

            Medicine m = medicineRepo.getMedicineById(i.getMedicineId());
            if (m == null) {
                throw new ResourceNotFoundException("Medicine not found!");
            }

//            if (m.getStock() < i.getQuantity()) {
//                throw new InsufficientStockException("Không đủ thuốc: " + m.getName());
//            }
//
//            m.setStock(m.getStock() - i.getQuantity());

            PrescriptionItem item = new PrescriptionItem();
            item.setMedicineId(m);
            item.setQuantity(i.getQuantity());
            item.setPrescriptionId(p);

            items.add(item);
        }

        Prescription saved = prescriptionRepo.addPrescription(p);

        for (PrescriptionItem it : items) {
            it.setPrescriptionId(saved);
            prescriptionItemRepo.save(it);
        }

        saved.setPrescriptionItemCollection(items);

        // 
        try {
            User patientUser = mr.getAppointmentId().getPatientId().getUserId(); 

            if (patientUser != null) {
                Map<String, String> notiParams = new HashMap<>();
                notiParams.put("username", patientUser.getUsername());
                notiParams.put("title", "Đơn thuốc mới");
                notiParams.put("content", "Bác sĩ vừa kê đơn thuốc mới cho bạn. Vui lòng kiểm tra!");
                notiParams.put("path", "/api/secure/prescriptions/" + saved.getId());

                this.notificationService.addNotification(notiParams);
            }
        } catch (Exception e) {
            System.err.println("Lỗi gửi thông báo: " + e.getMessage());
        }

        return saved;
    }
    

    public List<PrescriptionItem> getItems(Long prescriptionId) {
        return prescriptionItemRepo.getByPrescriptionId(prescriptionId);
    }

    @Transactional
    public PrescriptionItem addItem(Long prescriptionId, Long medicineId, Integer qty) {

        Prescription p = prescriptionRepo.getPrescriptionById(prescriptionId);
        if (p == null) {
            throw new ResourceNotFoundException("Prescription not found!");
        }
               
        Medicine m = medicineRepo.getMedicineById(medicineId);
        if (m == null) {
            throw new ResourceNotFoundException("Medicine not found!");
        }
        
//        if (m.getStock() < qty) {
//            throw new InsufficientStockException("Không đủ thuốc: " + m.getName());
//        }
//
//        m.setStock(m.getStock() - qty);

        PrescriptionItem item = new PrescriptionItem();
        item.setPrescriptionId(p);
        item.setMedicineId(m);
        item.setQuantity(qty);

        return prescriptionItemRepo.save(item);
    }

    @Transactional
    public PrescriptionItem updateItem(Long itemId, int newQty) {

        PrescriptionItem item = prescriptionItemRepo.getById(itemId);
        
        if (item == null) {
            throw new ResourceNotFoundException("PrescriptionItem not found!");
        }
        
        Medicine m = item.getMedicineId();

        int oldQty = item.getQuantity();

//        m.setStock(m.getStock() + oldQty);
//
//        if (m.getStock() < newQty) {
//            throw new InsufficientStockException("Không đủ thuốc: " + m.getName());
//        }
//
//        m.setStock(m.getStock() - newQty);

        item.setQuantity(newQty);

        return prescriptionItemRepo.save(item);
    }

    @Transactional
    public void deleteItem(Long itemId) {

        PrescriptionItem item = prescriptionItemRepo.getById(itemId);
        
        if (item == null) {
            throw new ResourceNotFoundException("PrescriptionItem not found!");
        }

        Medicine m = item.getMedicineId();

//        m.setStock(m.getStock() + item.getQuantity());

        prescriptionItemRepo.delete(item);
    }
   

    

    @Override
    @Transactional(readOnly = true)
    public Prescription getPrescriptionById(Long id) {
        Prescription prescription = this.prescriptionRepo.getPrescriptionById(id);

        if (prescription != null && prescription.getPrescriptionItemCollection() != null) {
            prescription.getPrescriptionItemCollection().size();

            for (PrescriptionItem item : prescription.getPrescriptionItemCollection()) {
                if (item.getMedicineId() != null) {
                    item.getMedicineId().getName();
                    item.getMedicineId().getSecureUrl();
                }
            }
        }

        return prescription;
    }

    @Override
    public void deletePrescription(Long id) {
        this.prescriptionRepo.deletePrescription(id);
    }

    @Override
    public long countPrescription(Map<String, String> params) {
        return this.prescriptionRepo.count(params, Prescription.class);
    }
}
