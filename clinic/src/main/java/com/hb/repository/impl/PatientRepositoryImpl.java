/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Patient;
import com.hb.repository.PatientRepository;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HUY
 */

@Repository
@Transactional
public class PatientRepositoryImpl implements PatientRepository{

    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Autowired
    private Environment env;
    
    @Override
    public List<Patient> getPatients(Map<String,String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Patient> q = session.createNamedQuery("Patient.findAll", Patient.class);
        
        if (params != null) {
            int pageSize = this.env.getProperty("patients.page_size", Integer.class);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page-1)*pageSize;
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
            
        }
        return q.getResultList();
    }

    @Override
    public Patient getPatientById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Patient> q = session.createNamedQuery("Patient.findById", Patient.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    @Override
    public Patient addPatient(Patient p) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(p);
        return p;
    }

    @Override
    public void updatePatient(Patient p) {
        Session session = this.factory.getObject().getCurrentSession();
        session.merge(p);
    }
    
}
