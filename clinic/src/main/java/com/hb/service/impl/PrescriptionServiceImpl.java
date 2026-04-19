/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.PrescriptionCreateRequest;
import com.hb.exception.InsufficientStockException;
import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.MedicalRecord;
import com.hb.pojo.Medicine;
import com.hb.pojo.Prescription;
import com.hb.pojo.PrescriptionItem;
import com.hb.repository.MedicalRecordRepository;
import com.hb.repository.MedicineRepository;
import com.hb.repository.PrescriptionItemRepository;
import com.hb.repository.PrescriptionRepository;
import com.hb.service.PrescriptionService;
import java.util.ArrayList;
import java.util.Date;
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
    private MedicalRecordRepository medicalRecordRepo;

    @Autowired
    private MedicineRepository medicineRepo;

    @Override
    @Transactional
    public Prescription addPrescription(PrescriptionCreateRequest req) {

        Prescription p = new Prescription();
        p.setCreatedAt(new Date());

        MedicalRecord mr = medicalRecordRepo.getMedicalRecordById(req.getMedicalRecordId());
        p.setMedicalRecordId(mr);

        List<PrescriptionItem> items = new ArrayList<>();

        for (var i : req.getItems()) {

            Medicine m = medicineRepo.getMedicineById(i.getMedicineId());

            if (m.getStock() < i.getQuantity()) {
                throw new InsufficientStockException("Không đủ thuốc: " + m.getName());
            }

            m.setStock(m.getStock() - i.getQuantity());

            PrescriptionItem item = new PrescriptionItem();
            item.setMedicineId(m);
            item.setQuantity(i.getQuantity());
            item.setPrescriptionId(p);

            items.add(item);
        }

        p.setPrescriptionItemCollection(items);

        return prescriptionRepo.addPrescription(p);
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
        
        if (m.getStock() < qty) {
            throw new InsufficientStockException("Không đủ thuốc: " + m.getName());
        }

        m.setStock(m.getStock() - qty);

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

        m.setStock(m.getStock() + oldQty);

        if (m.getStock() < newQty) {
            throw new InsufficientStockException("Không đủ thuốc: " + m.getName());
        }

        m.setStock(m.getStock() - newQty);

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

        m.setStock(m.getStock() + item.getQuantity());

        prescriptionItemRepo.delete(item);
    }
   

    @Override
    public List<Prescription> getPrescriptions(Map<String, String> params) {
        return this.prescriptionRepo.getPrescriptions(params);
    }

    @Override
    public Prescription getPrescriptionById(Long id) {
        return this.prescriptionRepo.getPrescriptionById(id);
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
