/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.LabTest;
import com.hb.repository.LabTestRepository;
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
 * @author DELL
 */
@Repository
@Transactional
public class LabTestRepositoryImpl extends BaseRepositoryImpl<LabTest> implements LabTestRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<LabTest> getLabTests(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT l FROM LabTest l WHERE 1=1");

        if (params != null && hasText(params.get("kw"))) {
            hql.append(" AND (LOWER(l.testName) LIKE :kw OR LOWER(l.unit) LIKE :kw OR LOWER(l.normalRange) LIKE :kw)");
        }

        Query<LabTest> q = session.createQuery(hql.toString(), LabTest.class);

        if (params != null && hasText(params.get("kw"))) {
            q.setParameter("kw", "%" + params.get("kw").trim().toLowerCase() + "%");
        }

        if (params != null && params.containsKey("pageSize")) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
        }
        return q.getResultList();
    }

    @Override
    public long count(Map<String, String> params, Class<LabTest> clazz) {
        Session session = this.factory.getObject().getCurrentSession();
        StringBuilder hql = new StringBuilder("SELECT COUNT(l.id) FROM LabTest l WHERE 1=1");

        if (params != null && hasText(params.get("kw"))) {
            hql.append(" AND (LOWER(l.testName) LIKE :kw OR LOWER(l.unit) LIKE :kw OR LOWER(l.normalRange) LIKE :kw)");
        }

        Query<Long> q = session.createQuery(hql.toString(), Long.class);

        if (params != null && hasText(params.get("kw"))) {
            q.setParameter("kw", "%" + params.get("kw").trim().toLowerCase() + "%");
        }

        return q.getSingleResult();
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    @Override
    public LabTest getLabTestById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(LabTest.class, id);
    }

    @Override
    public void addOrUpdateLabTest(LabTest test) {
        Session session = this.factory.getObject().getCurrentSession();
        if (test.getId() != null) {
            session.merge(test);
        } else {
            session.persist(test);
        }
    }

    @Override
    public void deleteLabTest(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        LabTest test = this.getLabTestById(id);
        if (test != null) {
            session.remove(test);
        }
    }
    
    @Override
    public Long countLabTests(Map<String, String> params) {
    Session session = this.factory.getObject().getCurrentSession();
    Query<Long> q;
    
   
    if (params != null && params.containsKey("testName") && !params.get("testName").isEmpty()) {
        q = session.createQuery("SELECT COUNT(l) FROM LabTest l WHERE l.testName LIKE :testName", Long.class);
        q.setParameter("testName", "%" + params.get("testName") + "%");
    } else {
        
        q = session.createQuery("SELECT COUNT(l) FROM LabTest l", Long.class);
    }
    
    return q.getSingleResult();
}
}
