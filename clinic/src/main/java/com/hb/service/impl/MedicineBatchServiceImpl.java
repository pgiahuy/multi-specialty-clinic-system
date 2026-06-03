/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.form.MedicineBatchForm;
import com.hb.enums.InventoryLogType;
import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.InventoryLog;
import com.hb.pojo.MedicineBatch;
import com.hb.repository.InventoryLogRepository;
import com.hb.repository.MedicineBatchRepository;
import com.hb.repository.MedicineRepository;
import com.hb.service.MedicineBatchService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HUY
 */
@Service
public class MedicineBatchServiceImpl implements MedicineBatchService {

    @Autowired
    private MedicineBatchRepository medicineBatchRepo;
    @Autowired
    private InventoryLogRepository inventoryLogRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MedicineBatch addOrUpdateMedicineBatch(MedicineBatchForm form) {
        MedicineBatch mb;
        if (form.getId() == null) {
            mb = new MedicineBatch();
        } else {
            mb = medicineBatchRepo.getMedicineBatchById(form.getId());
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
    public List<MedicineBatch> getExpiringBatches(Map<String, String> params) {
        return this.medicineBatchRepo.getExpiringBatches(params);
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
    @Transactional(rollbackFor = Exception.class)
    public void destroyMedicineBatch(Long id, String username) {
        MedicineBatch mb = medicineBatchRepo.getMedicineBatchById(id);
        if (mb==null) {
            throw new ResourceNotFoundException("Không tìm thấy lô thuốc!");
        }
        InventoryLog log = new InventoryLog();
        log.setMedicineId(mb.getMedicineId());
        log.setBatchId(mb);
        log.setChangeAmount(-mb.getQuantity());
        log.setReason(InventoryLogType.EXPIRED_DISPOSAL);
        log.setReferenceId(null);
        log.setCreatedAt(LocalDateTime.now());
        log.setCreatedBy(username);

        inventoryLogRepo.createInventoryLog(log);
        mb.setIsActive(false);
        
        this.medicineBatchRepo.saveOrUpdate(mb);
    }

    @Override
    public long countMedicineBatchs(Map<String, String> params) {
        return medicineBatchRepo.countMedicineBatchs(params);
    }

}
