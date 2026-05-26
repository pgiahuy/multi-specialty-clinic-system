/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.form.MedicineBatchForm;
import com.hb.pojo.MedicineBatch;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface MedicineBatchService {
    MedicineBatch addOrUpdateMedicineBatch(MedicineBatchForm form);
    List<MedicineBatch> getMedicineBatchs(Map<String, String> params);
    MedicineBatch getMedicineBatchById(Long id);
    void deleteMedicineBatch(Long id);
    long countMedicineBatchs(Map<String, String> params);
}
