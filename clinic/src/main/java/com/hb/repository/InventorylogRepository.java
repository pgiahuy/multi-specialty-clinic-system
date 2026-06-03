/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.InventoryLog;

/**
 *
 * @author HUY
 */
public interface InventoryLogRepository extends BaseRepository<InventoryLog>{
    void createInventoryLog(InventoryLog log);
    java.util.List<com.hb.pojo.InventoryLog> getUnconfirmedLogsByReferenceIdAndMedicine(Long referenceId, Long medicineId);
    void updateInventoryLog(InventoryLog log);
    
}
