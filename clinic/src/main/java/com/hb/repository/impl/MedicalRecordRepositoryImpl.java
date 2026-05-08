/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.MedicalRecord;
import com.hb.repository.MedicalRecordRepository;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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
public class MedicalRecordRepositoryImpl extends BaseRepositoryImpl<MedicalRecord> implements MedicalRecordRepository {

 
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<MedicalRecord> getMedicalRecords(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<MedicalRecord> q = session.createNamedQuery("MedicalRecord.findAll", MedicalRecord.class);

        if (params != null) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
        }

        return q.getResultList();
    }

    @Override
    public MedicalRecord addMedicalRecord(MedicalRecord m) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(m);
        return m;
    }

    @Override
    public MedicalRecord getMedicalRecordById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(MedicalRecord.class, id);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        Session session = this.factory.getObject().getCurrentSession();

        MedicalRecord m = session.get(MedicalRecord.class, id);

        if (m != null) {
            session.remove(m);
        } else {
            throw new RuntimeException("Medical record not found!");
        }
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<MedicalRecord> q = session.createQuery("SELECT m FROM MedicalRecord m WHERE m.appointmentId.patientId.id = :patientId", MedicalRecord.class);
        q.setParameter("patientId", patientId);
        return q.getResultList();
    }
}