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
    public long count(Map<String, String> params, Class<MedicineBatch> clazz) {
        Session session = this.factory.getObject().getCurrentSession();
        StringBuilder hql = new StringBuilder("SELECT COUNT(DISTINCT mb.id) FROM MedicineBatch mb LEFT JOIN mb.medicineId m WHERE mb.isActive = true");

        if (params != null && params.get("fromImport") != null && params.get("toImport") != null) {
            hql.append(" AND mb.importDate BETWEEN :fromImport AND :toImport");
        }

        if (params != null && params.get("fromExpiry") != null && params.get("toExpiry") != null) {
            hql.append(" AND mb.expiryDate BETWEEN :fromExpiry AND :toExpiry");
        }

        Query<Long> q = session.createQuery(hql.toString(), Long.class);
        if (params != null && params.get("fromImport") != null && params.get("toImport") != null) {
            q.setParameter("fromImport", LocalDate.parse(params.get("fromImport")));
            q.setParameter("toImport", LocalDate.parse(params.get("toImport")));
        }
        if (params != null && params.get("fromExpiry") != null && params.get("toExpiry") != null) {
            q.setParameter("fromExpiry", LocalDate.parse(params.get("fromExpiry")));
            q.setParameter("toExpiry", LocalDate.parse(params.get("toExpiry")));
        }

        return q.getSingleResult();
    }

    @Override
    public long countMedicineBatchs(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        StringBuilder hql = new StringBuilder("SELECT COUNT(DISTINCT mb.id) FROM MedicineBatch mb LEFT JOIN mb.medicineId m WHERE mb.isActive = true");

        if (params != null && hasText(params.get("kw"))) {
            hql.append(" AND (mb.batchCode LIKE :kw OR m.name LIKE :kw OR m.code LIKE :kw)");
        }

        if (params != null && hasText(params.get("fromImport")) && hasText(params.get("toImport"))) {
            hql.append(" AND mb.importDate BETWEEN :fromImport AND :toImport");
        }

        if (params != null && hasText(params.get("fromExpiry")) && hasText(params.get("toExpiry"))) {
            hql.append(" AND mb.expiryDate BETWEEN :fromExpiry AND :toExpiry");
        }

        Query<Long> q = session.createQuery(hql.toString(), Long.class);

        if (params != null && hasText(params.get("kw"))) {
            q.setParameter("kw", "%" + params.get("kw").trim() + "%");
        }
        if (params != null && hasText(params.get("fromImport")) && hasText(params.get("toImport"))) {
            q.setParameter("fromImport", LocalDate.parse(params.get("fromImport")));
            q.setParameter("toImport", LocalDate.parse(params.get("toImport")));
        }
        if (params != null && hasText(params.get("fromExpiry")) && hasText(params.get("toExpiry"))) {
            q.setParameter("fromExpiry", LocalDate.parse(params.get("fromExpiry")));
            q.setParameter("toExpiry", LocalDate.parse(params.get("toExpiry")));
        }

        return q.getSingleResult();
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

        if (params != null && hasText(params.get("kw"))) {
            var medicineJoin = root.join("medicineId", JoinType.LEFT);
            String keyword = "%" + params.get("kw").trim() + "%";
            predicates.add(b.or(
                    b.like(root.get("batchCode"), keyword),
                    b.like(medicineJoin.get("name"), keyword),
                    b.like(medicineJoin.get("code"), keyword)
            ));
        }

        if (params != null && hasText(params.get("fromImport")) && hasText(params.get("toImport"))) {
            LocalDate from = LocalDate.parse(params.get("fromImport"));
            LocalDate to = LocalDate.parse(params.get("toImport"));
            predicates.add(b.between(root.get("importDate"), from, to));
        }

        if (params != null && hasText(params.get("fromExpiry")) && hasText(params.get("toExpiry"))) {
            LocalDate from = LocalDate.parse(params.get("fromExpiry"));
            LocalDate to = LocalDate.parse(params.get("toExpiry"));
            predicates.add(b.between(root.get("expiryDate"), from, to));
        }

        predicates.add(b.isTrue(root.get("isActive")));

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
    public List<MedicineBatch> getExpiringBatches(Map<String, String> params) {
        if (params == null || !hasText(params.get("fromExpiry")) || !hasText(params.get("toExpiry"))) {
            return List.of();
        }

        Session session = this.factory.getObject().getCurrentSession();
        String hql = "SELECT mb FROM MedicineBatch mb LEFT JOIN FETCH mb.medicineId m "
                + "WHERE mb.isActive = true "
                + "AND mb.expiryDate BETWEEN :fromExpiry AND :toExpiry "
                + "ORDER BY mb.expiryDate ASC";

        Query<MedicineBatch> q = session.createQuery(hql, MedicineBatch.class);
        q.setParameter("fromExpiry", java.time.LocalDate.parse(params.get("fromExpiry")));
        q.setParameter("toExpiry", java.time.LocalDate.parse(params.get("toExpiry")));

        if (params != null && params.containsKey("pageSize") && hasText(params.get("pageSize"))) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            q.setMaxResults(pageSize);
            q.setFirstResult((page - 1) * pageSize);
        }

        return q.getResultList();
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

    @Override
    public MedicineBatch getAvailableBatchForMedicine(Long medicineId) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM MedicineBatch mb WHERE mb.medicineId.id = :medicineId "
                + "AND mb.quantityAvailable > 0 "
                + "AND mb.expiryDate > CURRENT_DATE "
                + "AND mb.isActive = true "
                + "ORDER BY mb.expiryDate ASC";
        Query<MedicineBatch> query = session.createQuery(hql, MedicineBatch.class);
        query.setParameter("medicineId", medicineId);
        query.setMaxResults(1);
        List<MedicineBatch> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
 

}
