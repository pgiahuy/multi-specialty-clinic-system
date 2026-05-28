/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.dto.request.form.MedicineBatchForm;
import com.hb.pojo.MedicineBatch;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface MedicineBatchRepository extends BaseRepository<MedicineBatch>{
    MedicineBatch saveOrUpdate(MedicineBatch m);
    List<MedicineBatch> getMedicineBatchs(Map<String,String> params);
    long countMedicineBatchs(Map<String, String> params);
    MedicineBatch getMedicineBatchById(Long id);
    void deleteMedicineBatch(Long id);
}
