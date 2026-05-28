/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.enums.SessionShift;
import com.hb.pojo.Shifts;
import com.hb.repository.ShiftRepository;
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
public class ShiftRepositoryImpl extends BaseRepositoryImpl<Shifts> implements ShiftRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    
 
    @Override
    public List<Shifts> getShifts(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        StringBuilder hql = new StringBuilder("SELECT s FROM Shifts s WHERE 1=1");

        if (params != null && hasText(params.get("session"))) {
            hql.append(" AND s.session = :session");
        }

        Query<Shifts> q = session.createQuery(hql.toString(), Shifts.class);

        if (params != null && hasText(params.get("session"))) {
            q.setParameter("session", SessionShift.valueOf(params.get("session").trim()));
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
    public long count(Map<String, String> params, Class<Shifts> clazz) {
        Session session = this.factory.getObject().getCurrentSession();
        StringBuilder hql = new StringBuilder("SELECT COUNT(s.id) FROM Shifts s WHERE 1=1");

        if (params != null && hasText(params.get("session"))) {
            hql.append(" AND s.session = :session");
        }

        Query<Long> q = session.createQuery(hql.toString(), Long.class);

        if (params != null && hasText(params.get("session"))) {
            q.setParameter("session", SessionShift.valueOf(params.get("session").trim()));
        }

        return q.getSingleResult();
    }

    @Override
    public Shifts saveOrUpdate(Shifts d) {
        Session session = this.factory.getObject().getCurrentSession();
        if (d.getId()==null) {
            session.persist(d);
            return d;
        }else{
            return session.merge(d);
        }
    }

    @Override
    public Shifts getShiftById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Shifts.class, id);
    }

    @Override
    public void deleteShift(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Shifts shift = session.get(Shifts.class, id);

        if (shift != null) {
            session.remove(shift);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
}
