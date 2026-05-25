/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Appointment;
import com.hb.pojo.Patient;
import com.hb.repository.StatsRepository;

import jakarta.ejb.Schedule;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HUY
 */
@Repository
@Transactional
public class StatsRepositoryImpl implements StatsRepository {

    @Autowired
    private LocalSessionFactoryBean fatory;

    @Override
    public List<Object[]> countPatientsByGender(LocalDate fromDate, LocalDate toDate) {

        Session session = this.fatory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Appointment> rootApp = q.from(Appointment.class);

        Join<Appointment, Patient> joinPatient = rootApp.join("patientId");
        Join<Appointment, Schedule> joinSchedule = rootApp.join("scheduleId");

        Predicate p1 = b.between(joinSchedule.get("date"), fromDate, toDate);
        Predicate p2 = b.equal(rootApp.get("status"), "COMPLETED");

        q.where(b.and(p1, p2));

        q.multiselect(
                joinPatient.get("gender"),
                b.countDistinct(joinPatient.get("id"))
        );
        q.groupBy(joinPatient.get("gender"));
        return session.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> countPatientsByAgeGroup(LocalDate fromDate, LocalDate toDate) {

        Session session = this.fatory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Appointment> rootApp = q.from(Appointment.class);

        Join<Appointment, Patient> joinPatient = rootApp.join("patientId");
        Join<Appointment, Schedule> joinSchedule = rootApp.join("scheduleId");

        Predicate p1 = b.between(joinSchedule.get("date"), fromDate, toDate);
        Predicate p2 = b.equal(rootApp.get("status"), "COMPLETED");

        q.where(b.and(p1, p2));

        q.multiselect(
                joinPatient.get("dob"),
                b.countDistinct(joinPatient.get("id"))
        );
        q.groupBy(joinPatient.get("dob"));
        return session.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> countPatientsBySpecialty(LocalDate fromDate, LocalDate toDate) {

        Session session = this.fatory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Appointment> rootApp = q.from(Appointment.class);

        Join<Appointment, Patient> joinPatient = rootApp.join("patientId");
        Join<Appointment, Schedule> joinSchedule = rootApp.join("scheduleId");

        Predicate p1 = b.between(joinSchedule.get("date"), fromDate, toDate);
        Predicate p2 = b.equal(rootApp.get("status"), "COMPLETED");

        q.where(b.and(p1, p2));

        q.multiselect(
                joinSchedule.get("specialtyId"),
                b.countDistinct(joinPatient.get("id"))
        );
        q.groupBy(joinSchedule.get("specialtyId"));
        return session.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> serviceUsageStats(LocalDate fromDate, LocalDate toDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Object[]> topDiseasesStats(LocalDate fromDate, LocalDate toDate, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Object[]> revenueStats(int year) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Object[]> revenueDetailsStats(LocalDate fromDate, LocalDate toDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
