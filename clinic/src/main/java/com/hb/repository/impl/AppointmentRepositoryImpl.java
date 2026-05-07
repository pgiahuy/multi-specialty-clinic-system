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
public class AppointmentRepositoryImpl extends BaseRepositoryImpl<Appointment> implements AppointmentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Appointment> getAppointments(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT DISTINCT a FROM Appointment a "
                + "LEFT JOIN FETCH a.patientId p "
                + "LEFT JOIN FETCH a.scheduleId s "
                + "LEFT JOIN FETCH s.doctorId d " 
                + "LEFT JOIN FETCH s.shiftId sh "
                + "LEFT JOIN FETCH s.roomId r " 
                + "LEFT JOIN FETCH r.areaId " 
                + "LEFT JOIN FETCH a.medicalRecord "
                + "WHERE 1=1 ");
        
        if (params.containsKey("currentUserId") && params.containsKey("currentUserRole")) {
            String role = params.get("currentUserRole");

            if ("ROLE_PATIENT".equals(role)) {
                System.out.println("HAAAAAAÂHAAAAAAAAAAAAAAAA");
                hql.append(" AND p.userId.id = :userId ");

            } else if ("ROLE_DOCTOR".equals(role)) {
                System.out.println("HAAAAAAÂHAAAAAAAAAAAAAAAA");
                hql.append(" AND d.userId.id = :userId ");
            }
        }

        if (params.containsKey("date")) {
            hql.append(" AND s.date = :date ");
        }

        Query<Appointment> q = session.createQuery(hql.toString(), Appointment.class);

        if (params.containsKey("currentUserId")) {
            q.setParameter("userId", Long.valueOf(params.get("currentUserId")));
        }
        if (params.containsKey("date")) {
            q.setParameter("date", java.sql.Date.valueOf(params.get("date")));
        }

        if (params.containsKey("pageSize")) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            q.setFirstResult((page - 1) * pageSize);
            q.setMaxResults(pageSize);
        }

        return q.getResultList();
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Appointment> q = session.createNamedQuery("Appointment.findById", Appointment.class);
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
