/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.enums.LabResultStatus;
import com.hb.pojo.LabResult;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.hb.repository.LabResultRepository;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author DELL
 */
@Repository
@Transactional
public class LabResultRepositoryImpl implements LabResultRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void addOrUpdateTestResult(LabResult lr) {
        Session session = this.factory.getObject().getCurrentSession();
        if (lr.getId() != null) {
            session.merge(lr);
        } else {
            session.persist(lr);
        }
    }

    @Override
    public LabResult getLabResultById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<LabResult> q = session.createNamedQuery("LabResult.findById", LabResult.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    @Override
    public List<LabResult> getLabResults(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT l FROM LabResult l "
                + "JOIN FETCH l.appointmentId a "
                + "JOIN FETCH a.patientId p "
                + "WHERE 1=1");

        if (params != null && params.containsKey("patientId") && !params.get("patientId").isEmpty()) {
            hql.append(" AND p.id = :patientId");
        }

        if (params != null && params.containsKey("kw") && !params.get("kw").isEmpty()) {
            hql.append(" AND LOWER(p.fullName) LIKE :kw");
        }

        String appointmentIdStr = params.get("appointmentId");
        if (appointmentIdStr != null && !appointmentIdStr.isEmpty()) {
            hql.append(" AND l.appointmentId.id = :appointmentId");
        }

        String dateStr = params != null ? params.get("date") : null;
        if (dateStr != null && !dateStr.isEmpty()) {
            hql.append(" AND l.createdAt >= :startOfDay AND l.createdAt <= :endOfDay");
        }

        String statusStr = params != null ? params.get("status") : null;
        if (statusStr != null && !statusStr.isEmpty() && !statusStr.equals("all")) {
            hql.append(" AND l.status = :status");
        }

        hql.append(" ORDER BY l.createdAt DESC");

        Query<LabResult> query = session.createQuery(hql.toString(), LabResult.class);

        if (params.containsKey("patientId") && !params.get("patientId").isEmpty()) {
            query.setParameter("patientId", Long.parseLong(params.get("patientId")));
        }

        if (params != null && params.containsKey("kw") && !params.get("kw").isEmpty()) {
           query.setParameter("kw", "%" + params.get("kw").toLowerCase() + "%");
        }

        if (appointmentIdStr != null && !appointmentIdStr.isEmpty()) {
            query.setParameter("appointmentId", Long.parseLong(appointmentIdStr));
        }

        if (dateStr != null && !dateStr.isEmpty()) {

            LocalDate localDate = LocalDate.parse(dateStr);
            
            LocalDateTime startOfDay = localDate.atStartOfDay();
            
            LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX); 

            query.setParameter("startOfDay", startOfDay);
            query.setParameter("endOfDay", endOfDay);
        }

        if (statusStr != null && !statusStr.isEmpty() && !statusStr.equals("all")) {
            
            String upperStatus = statusStr.toUpperCase();
            query.setParameter("status", LabResultStatus.valueOf(upperStatus));
        }

        return query.getResultList();
    }

    @Override
    public LabResult getLabResultsByAppointment(Long appointmentId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<LabResult> query = session.createQuery("SELECT r FROM LabResult r WHERE appointmentId.id = :appointmentId", LabResult.class);

        query.setParameter("appointmentId", appointmentId);
        return query.getSingleResult();
    }

}
