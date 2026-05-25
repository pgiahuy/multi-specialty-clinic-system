/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.form.MedicineForm;
import com.hb.pojo.Medicine;
import com.hb.repository.MedicineRepository;
import com.hb.service.MedicineService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */
@Service
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Medicine addOrUpdateMedicine(MedicineForm form) {

        Medicine medicine;
        if (form.getId() != null) {
            medicine = this.medicineRepo.getMedicineById(form.getId());

            if (medicine == null) {
                medicine = new Medicine();
            }
        } else {
            medicine = new Medicine();
        }

        medicine.setName(form.getName() != null ? form.getName().trim() : "");
        medicine.setCode(form.getCode() != null ? form.getCode().trim() : null);
        medicine.setPrice(form.getPrice());
        medicine.setMinStockAlert(form.getMinStockAlert());
        medicine.setUnit(form.getUnit() != null ? form.getUnit().trim() : "");

        if (form.getImage() != null && !form.getImage().isEmpty()) {
            if (medicine.getPublicId() != null && !medicine.getPublicId().isEmpty()) {
                this.cloudinaryService.deleteFile(medicine.getPublicId());
            }

            Map<?, ?> res = this.cloudinaryService.uploadFile(form.getImage(), "medicine");

            if (res != null && res.containsKey("secureUrl")) {
                medicine.setSecureUrl(res.get("secureUrl").toString());
                medicine.setPublicId(res.get("publicId").toString());
            }

        }
        return this.medicineRepo.addMedicine(medicine);
    }

    @Override
    public List<Medicine> getMedicines(Map<String, String> params) {
        return this.medicineRepo.getMedicines(params);
    }

    @Override
    public Medicine getMedicineById(Long id) {
        return this.medicineRepo.getMedicineById(id);
    }

    @Override
    public void deleteMedicine(Long id) {
        this.medicineRepo.deleteMedicine(id);
    }

    @Override
    public long countMedicines(Map<String, String> params) {
        return medicineRepo.count(params, Medicine.class);
    }
}
