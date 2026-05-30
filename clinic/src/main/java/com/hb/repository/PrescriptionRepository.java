/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.MedicalRecord;
import com.hb.pojo.Prescription;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author HUY
 */
public interface PrescriptionRepository extends BaseRepository<Prescription> {
    List<Prescription> getPrescriptions(Map<String,String> params);
    Prescription saveOrUpdate(Prescription m);
    Prescription getPrescriptionById(Long id);
    void deletePrescription(Long id);
}
