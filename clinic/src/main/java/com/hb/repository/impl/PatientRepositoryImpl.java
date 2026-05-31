/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.Appointment;
import com.hb.pojo.Doctor;
import com.hb.pojo.Patient;
import com.hb.pojo.Schedules;
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
        Join<Appointment, Schedules> scheduleJoin = appointmentJoin.join("scheduleId", JoinType.LEFT);
        Join<Schedules, Doctor> doctorJoin = scheduleJoin.join("doctorId", JoinType.LEFT);

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

}
