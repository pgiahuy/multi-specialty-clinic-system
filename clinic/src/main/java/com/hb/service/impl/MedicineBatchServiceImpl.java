/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.form.MedicineBatchForm;
import com.hb.pojo.MedicineBatch;
import com.hb.repository.MedicineBatchRepository;
import com.hb.repository.MedicineRepository;
import com.hb.service.MedicineBatchService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */
@Service
public class MedicineBatchServiceImpl implements MedicineBatchService {

    @Autowired
    private MedicineBatchRepository medicineBatchRepo;

    @Autowired
    private MedicineRepository medicineRepo;

    @Override
    public MedicineBatch addOrUpdateMedicineBatch(MedicineBatchForm form) {
        MedicineBatch mb;
        if (form.getId()==null) {
            mb = new MedicineBatch();
        }else{
            mb= medicineBatchRepo.getMedicineBatchById(form.getId());
        }
        
        mb.setBatchCode(form.getBatchCode());
        mb.setQuantity(form.getQuantity());
        mb.setImportDate(form.getImportDate());
        mb.setExpiryDate(form.getExpiryDate());
        mb.setMedicineId(form.getMedicine());
        return this.medicineBatchRepo.saveOrUpdate(mb);
    }

    @Override
    public List<MedicineBatch> getMedicineBatchs(Map<String, String> params) {
        return this.medicineBatchRepo.getMedicineBatchs(params);
    }

    @Override
    public MedicineBatch getMedicineBatchById(Long id) {
        return this.medicineBatchRepo.getMedicineBatchById(id);
    }

    @Override
    public void deleteMedicineBatch(Long id) {
        this.medicineBatchRepo.deleteMedicineBatch(id);
    }

    @Override
    public long countMedicineBatchs(Map<String, String> params) {
        return medicineBatchRepo.count(params, MedicineBatch.class);
    }

}
