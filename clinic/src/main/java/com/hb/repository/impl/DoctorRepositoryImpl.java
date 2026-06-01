/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.Doctor;
import com.hb.repository.DoctorRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
@Repository
@Transactional
public class DoctorRepositoryImpl extends BaseRepositoryImpl<Doctor> implements DoctorRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    @Override
    public List<Doctor> getAllDoctors() {
        Session session = this.factory.getObject().getCurrentSession();
        return session.createQuery("SELECT DISTINCT d FROM Doctor d LEFT JOIN FETCH d.specialtyCollection", Doctor.class)
                .getResultList();
    }

    @Override
    public List<Doctor> getDoctors(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT DISTINCT d.id FROM Doctor d LEFT JOIN d.specialtyCollection s WHERE 1=1");
        if (hasText(params.get("specialtyId"))) {
            hql.append(" AND s.id = :sId");
        }
        if (hasText(params.get("doctorName"))) {
            hql.append(" AND d.fullName LIKE :dName");
        }
        hql.append(" AND d.isActive=true");

        Query<Long> q = session.createQuery(hql.toString(), Long.class);
        if (hasText(params.get("specialtyId"))) {
            q.setParameter("sId", Long.valueOf(params.get("specialtyId")));
        }
        if (hasText(params.get("doctorName"))) {
            q.setParameter("dName", "%" + params.get("doctorName").trim() + "%");
        }

        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        q.setFirstResult((page - 1) * pageSize);
        q.setMaxResults(pageSize);
        List<Long> ids = q.getResultList();

        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        return session.createQuery("SELECT DISTINCT d FROM Doctor d LEFT JOIN FETCH d.specialtyCollection WHERE d.id IN :ids", Doctor.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public long count(Map<String, String> params, Class<Doctor> clazz) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT COUNT(DISTINCT d.id) FROM Doctor d LEFT JOIN d.specialtyCollection s WHERE 1=1");
        if (hasText(params.get("specialtyId"))) {
            hql.append(" AND s.id = :sId");
        }
        if (hasText(params.get("doctorName"))) {
            hql.append(" AND d.fullName LIKE :dName");
        }
        hql.append(" AND d.isActive=true");

        Query<Long> q = session.createQuery(hql.toString(), Long.class);
        if (hasText(params.get("specialtyId"))) {
            q.setParameter("sId", Long.valueOf(params.get("specialtyId")));
        }
        if (hasText(params.get("doctorName"))) {
            q.setParameter("dName", "%" + params.get("doctorName").trim() + "%");
        }

        return q.getSingleResult();
    }

    @Override
    public Doctor saveOrUpdate(Doctor d) {
        Session session = this.factory.getObject().getCurrentSession();
        if (d.getId() == null) {
            session.persist(d);
            return d;
        } else {
            return session.merge(d);
        }
    }

    @Override
    public Doctor getDoctorById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Doctor> cq = cb.createQuery(Doctor.class);
        Root<Doctor> root = cq.from(Doctor.class);
        cq.select(root).distinct(true);

        root.fetch("specialtyCollection", JoinType.LEFT);
        root.fetch("userId", JoinType.LEFT);

        cq.where(cb.equal(root.get("id"), id));

        Query<Doctor> q = session.createQuery(cq);

        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void deleteDoctor(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Doctor d = session.get(Doctor.class, id);
        if (d != null) {
            d.setIsActive(false);
            session.merge(d);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy bác sĩ!");
        }
    }

    @Override
    public Doctor getDoctorByUserId(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Doctor> cq = cb.createQuery(Doctor.class);
        Root<Doctor> root = cq.from(Doctor.class);

        root.fetch("specialtyCollection", JoinType.LEFT);
        Predicate p = cb.equal(root.get("userId").get("id"), id);
        cq.where(p);

        Query<Doctor> q = session.createQuery(cq);

        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
