/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.LabResults;
import com.hb.repository.LabTestResultRepository;
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

/**
 *
 * @author DELL
 */
@Repository
@Transactional
public class LabTestResultRepositoryImpl implements LabTestResultRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void addOrUpdateTestResult(LabResults lr) {
        Session session = this.factory.getObject().getCurrentSession();
        if (lr.getId() != null) {
            session.merge(lr);
        } else {
            session.persist(lr);
        }
    }

    @Override
    public LabResults getLabResultById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<LabResults> q = session.createNamedQuery("LabResults.findById", LabResults.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    @Override
    public List<LabResults> getTestResults(Long patientId, Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT l FROM LabResults l JOIN FETCH l.testId WHERE l.appointmentId.patientId.id = :patientId");

        String appointmentIdStr = params.get("appointmentId");
        if (appointmentIdStr != null && !appointmentIdStr.isEmpty()) {
            hql.append(" AND l.appointmentId.id = :appointmentId");
        }

        String startDateStr = params.get("startDate");
        String endDateStr = params.get("endDate");
        if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
            hql.append(" AND l.createdAt BETWEEN :startDate AND :endDate");
        }

        Query<LabResults> query = session.createQuery(hql.toString(), LabResults.class);

        query.setParameter("patientId", patientId);

        if (appointmentIdStr != null && !appointmentIdStr.isEmpty()) {
            query.setParameter("appointmentId", Long.parseLong(appointmentIdStr)); 
        }

        if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            query.setParameter("startDate", LocalDateTime.parse(startDateStr, formatter));
            query.setParameter("endDate", LocalDateTime.parse(endDateStr, formatter));
        }

        return query.getResultList();

    }

    @Override
    public List<LabResults> getLabResultsByAppointment(Long appointmentId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<LabResults> query = session.createQuery("SELECT r FROM LabResults r WHERE appointmentId.id = :appointmentId", LabResults.class);
        
        query.setParameter("appointmentId", appointmentId);
        return query.getResultList();
    }

}
