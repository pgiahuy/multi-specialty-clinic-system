/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Inventorylog;

/**
 *
 * @author HUY
 */
public interface InventorylogRepository extends BaseRepository<Inventorylog>{
    void createInventoryLog(Inventorylog log);
    
}
