/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Medicine;
import com.hb.repository.MedicineRepository;
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
public class MedicineRepositoryImpl extends BaseRepositoryImpl<Medicine> implements MedicineRepository {

 
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Medicine> getMedicines(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Medicine> q = session.createNamedQuery("Medicine.findAll", Medicine.class);

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
    public Medicine addMedicine(Medicine d) {
        Session session = this.factory.getObject().getCurrentSession();
        if (d.getId() == null) {
            session.persist(d);
        }else{
            session.merge(d);
        }
        return d;
    }

    @Override
    public Medicine getMedicineById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Medicine.class, id);
    }

    @Override
    public void deleteMedicine(Long id) {
        Session session = this.factory.getObject().getCurrentSession();

        Medicine d = session.get(Medicine.class, id);

        if (d != null) {
            session.remove(d);
        } else {
            throw new RuntimeException("Medicine not found!");
        }
    }
}