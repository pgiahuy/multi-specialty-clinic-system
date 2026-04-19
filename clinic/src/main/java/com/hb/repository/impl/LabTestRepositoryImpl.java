/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.LabTests;
import com.hb.repository.LabTestRepository;
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
 * @author DELL
 */
@Repository
@Transactional
public class LabTestRepositoryImpl extends BaseRepositoryImpl<LabTests> implements LabTestRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<LabTests> getLabTests(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<LabTests> q = session.createNamedQuery("LabTests.findAll", LabTests.class);

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
    public LabTests getLabTestById(Integer id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(LabTests.class, id);
    }

    @Override
    public void addOrUpdateLabTest(LabTests test) {
        Session session = this.factory.getObject().getCurrentSession();
        if (test.getId() != null) {
            session.merge(test);
        } else {
            session.persist(test);
        }
    }

    @Override
    public void deleteLabTest(Integer id) {
        Session session = this.factory.getObject().getCurrentSession();
        LabTests test = this.getLabTestById(id);
        if (test != null) {
            session.remove(test);
        }
    }
}
