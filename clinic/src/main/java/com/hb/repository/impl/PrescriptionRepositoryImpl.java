/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Prescription;
import com.hb.repository.PrescriptionRepository;
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
public class PrescriptionRepositoryImpl extends BaseRepositoryImpl<Prescription> implements PrescriptionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Prescription> getPrescriptions(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT p FROM Prescription p "
                + "JOIN FETCH p.medicalRecordId mr "
                + "JOIN mr.appointmentId a "
                + "WHERE 1=1");

        String role = params.get("currentUserRole");
        String userId = params.get("currentUserId");

        if ("ROLE_PATIENT".equals(role)) {
            hql.append(" AND a.patientId.userId.id = :userId AND p.status = 'PUBLIC'");
        } else if ("ROLE_DOCTOR".equals(role)) {
            hql.append(" AND a.scheduleId.doctorId.userId.id = :userId");
        }

        Query q = session.createQuery(hql.toString(), Prescription.class);

        if (role != null && userId != null) {
            q.setParameter("userId", Long.parseLong(userId));
        }

        if (params.containsKey("pageSize")) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            q.setMaxResults(pageSize);
            q.setFirstResult((page - 1) * pageSize);
        }

        return q.getResultList();
    }

    @Override
    public Prescription addPrescription(Prescription m) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(m);
        return m;
    }

    @Override
    public Prescription getPrescriptionById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Prescription.class, id);
    }

    @Override
    public void deletePrescription(Long id) {
        Session session = this.factory.getObject().getCurrentSession();

        Prescription m = session.get(Prescription.class, id);

        if (m != null) {
            session.remove(m);
        } else {
            throw new RuntimeException("Prescription not found!");
        }
    }
}
