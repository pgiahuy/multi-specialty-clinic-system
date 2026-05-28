/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.enums.AppointmentStatus;
import com.hb.pojo.Appointment;
import com.hb.repository.AppointmentRepository;

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

        if (params != null) {
            if (params.containsKey("currentUserId") && params.containsKey("currentUserRole")) {
                String role = params.get("currentUserRole");

                if ("ROLE_PATIENT".equals(role)) {

                    hql.append(" AND p.userId.id = :userId ");

                } else if ("ROLE_DOCTOR".equals(role)) {
                    hql.append(" AND a.status != :status ");
                    hql.append(" AND d.userId.id = :userId ");
                }
            }

            if (params.containsKey("date") && !params.get("date").isEmpty()) {
                hql.append(" AND s.date = :date ");
            }
            
            if (params.containsKey("scheduleId")) {
                hql.append(" AND s.id = :scheduleId");
            }

        }

        Query<Appointment> q = session.createQuery(hql.toString(), Appointment.class);

        if (params != null) {
            if (params.containsKey("currentUserId")) {
                q.setParameter("userId", Long.valueOf(params.get("currentUserId")));

                if ("ROLE_DOCTOR".equals(params.get("currentUserRole"))) {
                    q.setParameter("status", AppointmentStatus.PENDING);
                }
            }
            if (params.containsKey("date") && !params.get("date").isEmpty()) {
                q.setParameter("date", java.sql.Date.valueOf(params.get("date")));
            }
            
            if (params.containsKey("scheduleId")) {
                q.setParameter("scheduleId",Long.valueOf(params.get("scheduleId")));
            }

            if (params.containsKey("pageSize")) {
                int pageSize = Integer.parseInt(params.get("pageSize"));
                int page = Integer.parseInt(params.getOrDefault("page", "1"));
                q.setFirstResult((page - 1) * pageSize);
                q.setMaxResults(pageSize);
            }
        }

        return q.getResultList();
    }

    @Override
    public long count(Map<String, String> params, Class<Appointment> clazz) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT COUNT(DISTINCT a.id) FROM Appointment a "
                + "LEFT JOIN a.patientId p "
                + "LEFT JOIN a.scheduleId s "
                + "LEFT JOIN s.doctorId d WHERE 1=1 ");

        if (params != null) {
            if (params.containsKey("date")) {
                hql.append(" AND s.date = :date ");
            }
            if (params.containsKey("currentUserId") && params.containsKey("currentUserRole")) {
                String role = params.get("currentUserRole");
                if ("ROLE_PATIENT".equals(role)) {
                    hql.append(" AND p.userId.id = :userId ");
                } else if ("ROLE_DOCTOR".equals(role)) {
                    hql.append(" AND a.status != :status ");
                    hql.append(" AND d.userId.id = :userId ");
                }
            }
        }

        Query<Long> q = session.createQuery(hql.toString(), Long.class);

        if (params != null) {
            if (params.containsKey("date")) {
                q.setParameter("date", java.sql.Date.valueOf(params.get("date")));
            }
            if (params.containsKey("currentUserId")) {
                q.setParameter("userId", Long.valueOf(params.get("currentUserId")));
                if ("ROLE_DOCTOR".equals(params.get("currentUserRole"))) {
                    q.setParameter("status", AppointmentStatus.PENDING);
                }
            }
        }

        return q.getSingleResult();
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Appointment> q = session.createNamedQuery("Appointment.findById", Appointment.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    @Override
    public void addOrUpdateAppointment(Appointment a) {
        Session session = this.factory.getObject().getCurrentSession();
        if (a.getId() == null) {
            session.persist(a);
        } else {
            session.merge(a);
        }
    }

    @Override
    public boolean isPatientAlreadyBookedInSchedule(Long patientId, Long scheduleId) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "SELECT count(a.id) FROM Appointment a WHERE a.patientId.id = :patientId AND a.scheduleId.id = :scheduleId";

        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("patientId", patientId);
        query.setParameter("scheduleId", scheduleId);

        Long count = query.uniqueResult();
        return count != null && count > 0; 
    }

}
