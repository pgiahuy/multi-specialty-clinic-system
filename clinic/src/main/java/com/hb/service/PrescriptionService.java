/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.PrescriptionCreateRequest;
import com.hb.pojo.Prescription;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface PrescriptionService {
    Prescription addPrescription(PrescriptionCreateRequest req);
    List<Prescription> getPrescriptions(Map<String, String> params);
    Prescription getPrescriptionById(Long id);
    void deletePrescription(Long id);
}