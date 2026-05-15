/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.LabResults;
import com.hb.repository.LabTestRepository;
import com.hb.repository.LabTestResultRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DELL
 */
@Repository
@Transactional
public class LabTestResultRepositoryImpl implements LabTestResultRepository {
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void addOrUpdateTestResult(LabResults lr) {
        Session session = this.factory.getObject().getCurrentSession();
        if (lr.getId() != null) {
            session.merge(lr);
        } else
            session.persist(lr);
    }

    @Override
    public LabResults getLabResultById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<LabResults> q = session.createNamedQuery("LabResults.findById", LabResults.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }
    
}
