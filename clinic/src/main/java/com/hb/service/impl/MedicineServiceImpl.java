/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.pojo.Medicine;
import com.hb.repository.MedicineRepository;
import com.hb.service.MedicineService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public Medicine addMedicine(Map<String, String> params, MultipartFile avatar) {

        Medicine m = new Medicine();

        m.setName(params.getOrDefault("name", "").trim());

        String stockStr = params.get("stock");
        if (stockStr != null && !stockStr.isEmpty()) {
            try {
                m.setStock(Integer.valueOf(stockStr));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Số lượng không hợp lệ");
            }
        }

        String dateStr = params.get("expirationDate");

        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                Date expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                m.setExpirationDate(expirationDate);
            } catch (ParseException e) {
                throw new RuntimeException("Ngày hết hạn không hợp lệ");
            }
        }

        if ( !avatar.isEmpty()) {
            Map res = this.cloudinaryService.uploadFile(avatar, "avatar");
            
            m.setSecureUrl(res.get("secureUrl").toString());
            m.setPublicId(res.get("publicId").toString());
        }

        return this.medicineRepo.addMedicine(m);
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
