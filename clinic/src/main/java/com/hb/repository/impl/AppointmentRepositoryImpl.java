/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Appointment;
import com.hb.repository.AppointmentRepository;

import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AppointmentRepositoryImpl extends BaseRepositoryImpl<Appointment> implements AppointmentRepository{

    @Autowired
    private LocalSessionFactoryBean factory;
    

    @Override
    public List<Appointment> getAppointments(Map<String,String> params) {
        
        Session session = this.factory.getObject().getCurrentSession();
        Query<Appointment> q = session.createNamedQuery("Appointment.findAll", Appointment.class);
        
        
        if (params!=null) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page-1)*pageSize;
            
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
        }
        return q.getResultList();
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Appointment> q = session.createNamedQuery("Appointment.findById",Appointment.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    @Override
    public Appointment addAppointment(Appointment a) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(a);
        return a;
    }


    
}
