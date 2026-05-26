/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Doctor;
import com.hb.pojo.Specialty;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.hb.repository.SpecialtyRepository;

/**
 *
 * @author HUY
 */
@Repository
@Transactional
public class SpecialtyRepositoryImpl extends BaseRepositoryImpl<Specialty> implements SpecialtyRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    

    @Override
    public List<Specialty> getSpecialties(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT DISTINCT s FROM Specialty s LEFT JOIN FETCH s.doctorCollection d LEFT JOIN FETCH s.idHod hod WHERE 1=1");

        if (params != null && hasText(params.get("kw"))) {
            hql.append(" AND s.name LIKE :kw");
        }
        if (params != null && hasText(params.get("specialtyName"))) {
            hql.append(" AND s.name LIKE :specialtyName");
        }
        if (params != null && hasText(params.get("doctorName"))) {
            hql.append(" AND hod.fullName LIKE :doctorName");
        }

        Query<Specialty> q = session.createQuery(hql.toString(), Specialty.class);

        if (params != null && hasText(params.get("kw"))) {
            q.setParameter("kw", "%" + params.get("kw").trim() + "%");
        }
        if (params != null && hasText(params.get("specialtyName"))) {
            q.setParameter("specialtyName", "%" + params.get("specialtyName").trim() + "%");
        }
        if (params != null && hasText(params.get("doctorName"))) {
            q.setParameter("doctorName", "%" + params.get("doctorName").trim() + "%");
        }

        if (params != null && params.containsKey("pageSize")) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            q.setFirstResult((page - 1) * pageSize);
            q.setMaxResults(pageSize);
        }

        return q.getResultList();
    }

    @Override
    public long count(Map<String, String> params, Class<Specialty> clazz) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT COUNT(DISTINCT s.id) FROM Specialty s LEFT JOIN s.idHod hod WHERE 1=1");

        if (params != null && hasText(params.get("kw"))) {
            hql.append(" AND s.name LIKE :kw");
        }
        if (params != null && hasText(params.get("specialtyName"))) {
            hql.append(" AND s.name LIKE :specialtyName");
        }
        if (params != null && hasText(params.get("doctorName"))) {
            hql.append(" AND hod.fullName LIKE :doctorName");
        }

        Query<Long> q = session.createQuery(hql.toString(), Long.class);
        if (params != null && hasText(params.get("kw"))) {
            q.setParameter("kw", "%" + params.get("kw").trim() + "%");
        }
        if (params != null && hasText(params.get("specialtyName"))) {
            q.setParameter("specialtyName", "%" + params.get("specialtyName").trim() + "%");
        }
        if (params != null && hasText(params.get("doctorName"))) {
            q.setParameter("doctorName", "%" + params.get("doctorName").trim() + "%");
        }

        return q.getSingleResult();
    }

    @Override
    public Specialty getSpecialtieById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Specialty> q = session.createNamedQuery("Specialty.findById", Specialty.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    @Override
    public Specialty saveOrUpdate(Specialty s) {
        Session session = this.factory.getObject().getCurrentSession();
        if (s.getId()==null) {
            session.persist(s);
            return s;
        }else{
            return session.merge(s);
        }
    }

    @Override
    public List<Specialty> getAllById(List<Long> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.createQuery("FROM Specialty s WHERE s.id IN :ids", Specialty.class)
                .setParameter("ids", ids)
                .getResultList();
    }

}
