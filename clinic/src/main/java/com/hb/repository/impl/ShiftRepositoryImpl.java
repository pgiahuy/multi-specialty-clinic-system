/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

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
        Query<Shifts> q = session.createNamedQuery("Shifts.findAll",Shifts.class);
        
        
        if(params!= null){
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt( params.getOrDefault("page", "1"));
            int start = (page-1)*pageSize;
            
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
            
        }
        System.out.println("===========");
        System.out.println(q.getResultList());
        System.out.println("===========");
        return q.getResultList();
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
    }
    
}
