/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.form.MedicineForm;
import com.hb.dto.response.MedicineResponse;
import com.hb.pojo.Medicine;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author HUY
 */
public interface MedicineService {
    Medicine addOrUpdateMedicine(MedicineForm medicineForm);
    List<Medicine> getMedicines(Map<String, String> params);
    List<MedicineResponse> getMedicinesWithStock(Map<String, String> params);
    Medicine getMedicineById(Long id);
    void deleteMedicine(Long id);
    long countMedicines(Map<String, String> params);
}