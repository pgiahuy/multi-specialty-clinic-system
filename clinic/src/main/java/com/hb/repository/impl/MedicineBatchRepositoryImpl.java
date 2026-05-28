/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.MedicineBatch;
import com.hb.repository.MedicineBatchRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
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
@Transactional
@Repository
public class MedicineBatchRepositoryImpl implements MedicineBatchRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    

    @Override
    public MedicineBatch saveOrUpdate(MedicineBatch m) {
        Session session = this.factory.getObject().getCurrentSession();
        if (m.getId() == null) {
            session.persist(m);
            return m;
        } else {
            return session.merge(m);
        }
    }

    @Override
    public List<MedicineBatch> getMedicineBatchs(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<MedicineBatch> q = b.createQuery(MedicineBatch.class);
        Root<MedicineBatch> root = q.from(MedicineBatch.class);

        root.fetch("medicineId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (params.get("fromImport") != null && params.get("toImport") != null) {
            LocalDate from = LocalDate.parse(params.get("fromImport"));
            LocalDate to = LocalDate.parse(params.get("toImport"));
            predicates.add(b.between(root.get("importDate"), from, to));
        }

        if (params.get("fromExpiry") != null && params.get("toExpiry") != null) {
            LocalDate from = LocalDate.parse(params.get("fromExpiry"));
            LocalDate to = LocalDate.parse(params.get("toExpiry"));
            predicates.add(b.between(root.get("expiryDate"), from, to));
        }

        q.where(predicates.toArray(new Predicate[0]));
        q.orderBy(b.desc(root.get("id")));

        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        int page = Integer.parseInt(params.getOrDefault("page", "1"));

        return session.createQuery(q)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
    
    @Override
    public long count(Map<String, String> params, Class<MedicineBatch> clazz) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(DISTINCT mb.id) FROM MedicineBatch mb LEFT JOIN mb.medicineId m WHERE 1=1";
        Query<Long> q = session.createQuery(hql, Long.class);
        return q.getSingleResult();
    }

    @Override
    public MedicineBatch getMedicineBatchById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<MedicineBatch> q = session.createNamedQuery("MedicineBatch.findById", MedicineBatch.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    @Override
    public void deleteMedicineBatch(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        MedicineBatch d = session.get(MedicineBatch.class, id);
        if (d != null) {
            session.remove(d);
        } else {
            throw new ResourceNotFoundException("MedicineBatch not found!");
        }
    }

}
