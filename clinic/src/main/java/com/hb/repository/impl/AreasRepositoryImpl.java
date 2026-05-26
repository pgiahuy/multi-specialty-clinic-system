/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Areas;
import com.hb.repository.AreasRepository;
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
public class AreasRepositoryImpl extends BaseRepositoryImpl<Areas> implements AreasRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Areas> getAreas(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Areas> q = session.createNamedQuery("Areas.findAll", Areas.class);

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
    public Areas getAreasById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Areas.class, id);
    }

    @Override
    public Areas saveOrUpdate(Areas a) {
        Session session = this.factory.getObject().getCurrentSession();
        if (a.getId()==null) {
            session.persist(a);
            return a;
        }else{
            return session.merge(a);
        }
    }
    
    @Override
    public void deleteAreas(Long id) {
        Session session = this.factory.getObject().getCurrentSession();

        Areas m = session.get(Areas.class, id);

        if (m != null) {
            session.remove(m);
        } else {
            throw new RuntimeException("Medical record not found!");
        }
    }
}

