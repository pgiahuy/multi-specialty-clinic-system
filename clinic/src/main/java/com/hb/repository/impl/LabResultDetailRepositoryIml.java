/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.LabResultDetail;
import com.hb.repository.LabResultDetailRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author DELL
 */
@Repository
public class LabResultDetailRepositoryIml implements LabResultDetailRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void addOrUpdate(LabResultDetail detail) {
        Session session = this.factory.getObject().getCurrentSession();
        
        if (detail.getId() == null) {
            session.persist(detail);
        } else {
            session.merge(detail);
        }
        
    }
}
