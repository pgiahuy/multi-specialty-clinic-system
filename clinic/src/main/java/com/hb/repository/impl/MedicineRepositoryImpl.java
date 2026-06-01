/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.dto.response.MedicineResponse;
import com.hb.pojo.Medicine;
import com.hb.pojo.MedicineBatch;
import com.hb.repository.MedicineRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
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

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    @Override
    public List<MedicineBatch> getAvailableBatches(Long medicineId, LocalDate minExpiryDate) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "SELECT mb FROM MedicineBatch mb "
                + "WHERE mb.medicineId.id = :medicineId "
                + "AND mb.quantity > 0 "
                + "AND mb.expiryDate >= :minExpiryDate "
                + "ORDER BY mb.expiryDate ASC, mb.importDate ASC";

        return session.createQuery(hql, MedicineBatch.class)
                .setParameter("medicineId", medicineId)
                .setParameter("minExpiryDate", minExpiryDate)
                .getResultList();
    }

    
    @Override
    public List<MedicineResponse> getMedicinesWithStock(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        StringBuilder hql = new StringBuilder(
                "SELECT new com.hb.dto.response.MedicineResponse(m.id, m.code, m.name, m.price, m.unit, SUM(mb.quantity)) "
                + "FROM Medicine m "
                + "LEFT JOIN MedicineBatch mb ON mb.medicineId = m "
                + "WHERE 1=1"
        );

        if (params != null && hasText(params.get("kw"))) {
            hql.append(" AND (m.name LIKE :kw OR m.code LIKE :kw)");
        }
        hql.append(" GROUP BY m.id, m.code, m.name, m.price, m.unit");

        Query<MedicineResponse> q = session.createQuery(hql.toString(), MedicineResponse.class);

        if (params != null && hasText(params.get("kw"))) {
            q.setParameter("kw", "%" + params.get("kw").trim() + "%");
        }
        if (params != null && params.containsKey("pageSize") && hasText(params.get("pageSize"))) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
        }

        return q.getResultList();
    }

    @Override
    public List<Medicine> getMedicines(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("FROM Medicine m WHERE 1=1");
        if (params != null && hasText(params.get("kw"))) {
            hql.append(" AND (m.name LIKE :kw OR m.code LIKE :kw)");
        }

        Query<Medicine> q = session.createQuery(hql.toString(), Medicine.class);

        if (params != null && hasText(params.get("kw"))) {
            q.setParameter("kw", "%" + params.get("kw").trim() + "%");
        }

        if (params != null && params.containsKey("pageSize") && hasText(params.get("pageSize"))) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
        }

        return q.getResultList();
    }

    @Override
    public Medicine addMedicine(Medicine m) {
        Session session = this.factory.getObject().getCurrentSession();
        if (m.getId() == null) {
            session.persist(m);
            return m;
        } else {
            return session.merge(m);
        }
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

    @Override
    public long countMedicines(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT COUNT(m) FROM Medicine m WHERE 1=1");
        if (params != null && hasText(params.get("kw"))) {
            hql.append(" AND (m.name LIKE :kw OR m.code LIKE :kw)");
        }

        Query<Long> q = session.createQuery(hql.toString(), Long.class);
        if (params != null && hasText(params.get("kw"))) {
            q.setParameter("kw", "%" + params.get("kw").trim() + "%");
        }

        return q.getSingleResult();
    }

}
