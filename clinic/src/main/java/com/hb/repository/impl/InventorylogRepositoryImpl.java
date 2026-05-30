/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.InventoryLog;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import com.hb.repository.InventoryLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HUY
 */

@Repository
@Transactional
public class InventoryLogRepositoryImpl implements InventoryLogRepository{

    @Autowired  
    private LocalSessionFactoryBean factory;

    

    @Override
    public void createInventoryLog(InventoryLog log) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(log);
    }

    @Override
    public long count(Map<String, String> params, Class<InventoryLog> clazz) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}
