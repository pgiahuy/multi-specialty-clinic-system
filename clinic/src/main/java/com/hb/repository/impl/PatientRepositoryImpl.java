/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.enums.PatientRelationship;
import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.Appointment;
import com.hb.pojo.Doctor;
import com.hb.pojo.Patient;
import com.hb.pojo.Schedule;
import com.hb.pojo.User;
import com.hb.repository.PatientRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
public class PatientRepositoryImpl extends BaseRepositoryImpl<Patient> implements PatientRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    @Override
    public List<Patient> getPatients(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Patient> cq = cb.createQuery(Patient.class);
        Root<Patient> root = cq.from(Patient.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("isActive"), true));
        if (params != null && hasText(params.get("patientName"))) {
            String kw = "%" + params.get("patientName").trim() + "%";
            predicates.add(cb.or(
                    cb.like(root.get("fullName").as(String.class), kw),
                    cb.like(root.get("cccd").as(String.class), kw)
            ));
        }
        if (params != null && hasText(params.get("gender"))) {
            predicates.add(cb.equal(root.get("gender"), params.get("gender").trim()));
        }
 
        cq.distinct(true);
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("id")));

        Query<Patient> q = session.createQuery(cq);

        if (params != null && params.containsKey("pageSize") && hasText(params.get("pageSize"))) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
        }

        return q.getResultList();
    }

    @Override
    public List<Patient> getPatientsForDoctor(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Patient> cq = cb.createQuery(Patient.class);
        Root<Patient> root = cq.from(Patient.class);

        Join<Patient, Appointment> appointmentJoin = root.join("appointmentCollection", JoinType.LEFT);
        Join<Appointment, Schedule> scheduleJoin = appointmentJoin.join("scheduleId", JoinType.LEFT);
        Join<Schedule, Doctor> doctorJoin = scheduleJoin.join("doctorId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("isActive"), true));
        if (params != null && hasText(params.get("patientName"))) {
            String kw = "%" + params.get("patientName").trim() + "%";
            predicates.add(cb.or(
                    cb.like(root.get("fullName").as(String.class), kw),
                    cb.like(root.get("cccd").as(String.class), kw)
            ));
        }
        if (params != null && hasText(params.get("doctorId"))) {
            predicates.add(cb.equal(doctorJoin.get("id"), params.get("doctorId").trim()));
        }
        if (params != null && hasText(params.get("gender"))) {
            predicates.add(cb.equal(root.get("gender"), params.get("gender").trim()));
        }

        cq.distinct(true);
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("id")));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Patient getPatientById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Patient> q = session.createNamedQuery("Patient.findById", Patient.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    @Override
    public Patient saveOrUpdate(Patient p) {
        Session session = this.factory.getObject().getCurrentSession();
        if (p.getId() == null) {
            session.persist(p);
            return p;
        } else {
            return session.merge(p);
        }
    }

    @Override
    public void deletePatient(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Patient patient = session.get(Patient.class, id);

        if (patient != null) {
            patient.setIsActive(false);
            session.merge(patient);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy bệnh nhân có ID: " + id);
        }
    }

    @Override
    public long count(Map<String, String> params, Class<Patient> clazz) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Patient> root = cq.from(Patient.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("isActive"), true));
        if (params != null && hasText(params.get("patientName"))) {
            String kw = "%" + params.get("patientName").trim() + "%";
            predicates.add(cb.or(
                    cb.like(root.get("fullName").as(String.class), kw),
                    cb.like(root.get("cccd").as(String.class), kw)
            ));
        }
        if (params != null && hasText(params.get("gender"))) {
            predicates.add(cb.equal(root.get("gender"), params.get("gender").trim()));
        }

        cq.select(cb.countDistinct(root)).where(predicates.toArray(new Predicate[0]));
        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public List<Patient> getPatientsByUserId(Long userId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("SELECT p FROM Patient p WHERE p.userId.id = :id "
                + " AND p.isActive = true", Patient.class);
        query.setParameter("id", userId);
        return query.getResultList();
    }

    @Override
    public boolean isExistedCCCD(String cccd) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("SELECT COUNT(p) FROM Patient p WHERE p.cccd =: cccd", Long.class);
        query.setParameter("cccd", cccd);
        
        Long count = (Long) query.uniqueResult();
        
        return count != null && count > 0;
    }

    @Override
    public boolean isExistedForSelf(User u) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("SELECT COUNT(p) FROM Patient p WHERE p.userId.id = :userId "
                                                                        + "AND p.relationship = :relationship", Long.class);
        query.setParameter("userId", u.getId());
        query.setParameter("relationship", PatientRelationship.SELF);
        
        Long count = (Long) query.uniqueResult();
        
        return count != null && count > 0;
        
    }

}
