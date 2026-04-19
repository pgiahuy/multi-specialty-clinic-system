/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Prescription;
import com.hb.repository.PrescriptionRepository;
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
public class PrescriptionRepositoryImpl extends BaseRepositoryImpl<Prescription>  implements PrescriptionRepository {


    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Prescription> getPrescriptions(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Prescription> q = session.createNamedQuery("Prescription.findAll", Prescription.class);

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
    public Prescription addPrescription(Prescription m) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(m);
        return m;
    }

    @Override
    public Prescription getPrescriptionById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Prescription.class, id);
    }

    @Override
    public void deletePrescription(Long id) {
        Session session = this.factory.getObject().getCurrentSession();

        Prescription m = session.get(Prescription.class, id);

        if (m != null) {
            session.remove(m);
        } else {
            throw new RuntimeException("Prescription not found!");
        }
    }
}