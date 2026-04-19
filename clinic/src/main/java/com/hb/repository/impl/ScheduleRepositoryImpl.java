/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Schedules;
import com.hb.repository.ScheduleRepository;
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
 * @author HUY
 */

@Repository
@Transactional
public class ScheduleRepositoryImpl extends BaseRepositoryImpl<Schedules> implements ScheduleRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    


    @Override
    public List<Schedules> getSchedules(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Schedules> q = session.createNamedQuery("Schedules.findAllWithDetails",Schedules.class);
        
        
        if(params!= null){
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt( params.getOrDefault("page", "1"));
            int start = (page-1)*pageSize;
            
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
            
        }
        System.out.println("============");
        System.out.println(q.getResultList());
        System.out.println("============");
        return q.getResultList();
    }

    @Override
    public Schedules addSchedule(Schedules d) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(d);
        return d;
    }

    @Override
    public Schedules getScheduleById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Schedules.class, id);
    }

    @Override
    public void deleteSchedule(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Schedules s = session.get(Schedules.class, id);
        if (s != null) {
            session.remove(s);
        }
    }
    
}