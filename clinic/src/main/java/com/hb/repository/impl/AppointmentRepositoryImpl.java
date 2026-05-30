/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.enums.AppointmentStatus;
import com.hb.pojo.Appointment;
import com.hb.pojo.Patient;
import com.hb.pojo.Schedules;
import com.hb.repository.AppointmentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    @Override
    public List<Appointment> getAppointments(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);

        root.fetch("patientId", JoinType.LEFT);
        Join<Appointment, Patient> patientJoin = root.join("patientId", JoinType.LEFT);

        root.fetch("scheduleId", JoinType.LEFT);
        Join<Appointment, Schedules> scheduleJoin = root.join("scheduleId", JoinType.LEFT);
        scheduleJoin.fetch("doctorId", JoinType.LEFT);
        scheduleJoin.fetch("shiftId", JoinType.LEFT);
        scheduleJoin.fetch("roomId", JoinType.LEFT);

        Join<Schedules, ?> doctorJoin = scheduleJoin.join("doctorId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            if (params.containsKey("currentUserId") && params.containsKey("currentUserRole")) {
                String role = params.get("currentUserRole");

                if ("ROLE_PATIENT".equals(role)) {
                    predicates.add(cb.equal(patientJoin.get("userId").get("id"), Long.valueOf(params.get("currentUserId"))));
                } else if ("ROLE_DOCTOR".equals(role)) {
                    predicates.add(cb.equal(doctorJoin.get("userId").get("id"), Long.valueOf(params.get("currentUserId"))));
                }
            }
          

            if (hasText(params.get("status"))) {
                predicates.add(cb.equal(root.get("status"), AppointmentStatus.valueOf(params.get("status").trim().toUpperCase())));
            }

            if (params.containsKey("date") && !params.get("date").isEmpty()) {
                predicates.add(cb.equal(scheduleJoin.get("date"), java.sql.Date.valueOf(params.get("date"))));
            }

            if (hasText(params.get("kw"))) {
                String kw = "%" + params.get("kw").trim() + "%";
                predicates.add(cb.or(
                        cb.like(patientJoin.get("fullName").as(String.class), kw),
                        cb.like(doctorJoin.get("fullName").as(String.class), kw)
                ));
            }

            if (params.containsKey("scheduleId")) {
                predicates.add(cb.equal(scheduleJoin.get("id"), Long.valueOf(params.get("scheduleId"))));
                if ("ROLE_DOCTOR".equals(params.get("currentUserRole"))) {
                    predicates.add(cb.notEqual(root.get("status"), AppointmentStatus.UN_PAID));
                }
            }
        }

        cq.select(root).distinct(true);
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("id")));

        Query<Appointment> q = session.createQuery(cq);

        if (params != null && params.containsKey("pageSize")) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            q.setFirstResult((page - 1) * pageSize);
            q.setMaxResults(pageSize);
        }

        return q.getResultList();
    }

    @Override
    public long count(Map<String, String> params, Class<Appointment> clazz) {
        return countAppointments(params);
    }

    @Override
    public long countAppointments(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT COUNT(DISTINCT a.id) FROM Appointment a "
                + "LEFT JOIN a.patientId p "
                + "LEFT JOIN a.scheduleId s "
                + "LEFT JOIN s.doctorId d WHERE 1=1 ");

        if (params != null) {
            if (params.containsKey("date")) {
                hql.append(" AND s.date = :date ");
            }
            if (hasText(params.get("kw"))) {
                hql.append(" AND (p.fullName LIKE :kw OR d.fullName LIKE :kw)");
            }
            if (params.containsKey("scheduleId")) {
                hql.append(" AND s.id = :scheduleId ");
            }
            if (params.containsKey("currentUserId") && params.containsKey("currentUserRole")) {
                String role = params.get("currentUserRole");
                if ("ROLE_PATIENT".equals(role)) {
                    hql.append(" AND p.userId.id = :userId ");
                } else if ("ROLE_DOCTOR".equals(role)) {
                    hql.append(" AND d.userId.id = :userId ");
                }
            }
            if (hasText(params.get("status"))) {
                hql.append(" AND a.status = :status ");
            }
        }

        Query<Long> q = session.createQuery(hql.toString(), Long.class);

        if (params != null) {
            if (params.containsKey("date")) {
                q.setParameter("date", java.sql.Date.valueOf(params.get("date")));
            }
            if (hasText(params.get("kw"))) {
                q.setParameter("kw", "%" + params.get("kw").trim() + "%");
            }
            if (params.containsKey("scheduleId")) {
                q.setParameter("scheduleId", Long.valueOf(params.get("scheduleId")));
            }
            if (params.containsKey("currentUserId")) {
                q.setParameter("userId", Long.valueOf(params.get("currentUserId")));
            }
            if (hasText(params.get("status"))) {
                q.setParameter("status", AppointmentStatus.valueOf(params.get("status").trim().toUpperCase()));
            }
        }

        return q.getSingleResult();
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Appointment> q = session.createNamedQuery("Appointment.findById", Appointment.class);
        q.setParameter("id", id);

        List<Appointment> appointments = q.getResultList();
        return appointments.isEmpty() ? null : appointments.get(0);
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
        return count > 0; 
    }

    @Override
    public List<Appointment> getAppointmentByPatientId(Long patientId, Map<String, String> params) {
         Session session = this.factory.getObject().getCurrentSession();
        
        
        StringBuilder hql = new StringBuilder("FROM Appointment a WHERE a.patientId.id = :patientId");
        
        
        String status = params.get("status");
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        
        
        if (status != null && !status.isEmpty()) {
            hql.append(" AND a.status = :status");
        }
        
       
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            hql.append(" AND a.scheduleId.date >= :startDate AND a.cr < :endDate");
        }
        
       
        hql.append(" ORDER BY a.scheduleId.date DESC");
        
        
        Query<Appointment> query = session.createQuery(hql.toString(), Appointment.class);
        query.setParameter("patientId", patientId);
        
        
        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }
        
        
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            
            
            query.setParameter("startDate", start);
            query.setParameter("endDate", end.plusDays(1));
        }
        
        return query.getResultList();
    }

    

}
