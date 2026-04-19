/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.Medicine;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author HUY
 */
public interface MedicineService {
    Medicine addMedicine(Map<String, String> params, MultipartFile image);
    List<Medicine> getMedicines(Map<String, String> params);
    Medicine getMedicineById(Long id);
    void deleteMedicine(Long id);
}