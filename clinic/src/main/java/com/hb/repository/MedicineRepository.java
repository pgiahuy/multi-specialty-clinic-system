/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Medicine;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface MedicineRepository extends BaseRepository<Medicine>{
    List<Medicine> getMedicines(Map<String,String> params);
    Medicine addMedicine(Medicine d);
    Medicine getMedicineById(Long id);
    void deleteMedicine(Long id);
}
