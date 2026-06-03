/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.enums.PaymentStatus;
import com.hb.pojo.Appointment;
import com.hb.pojo.Patient;
import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItem;
import com.hb.pojo.Schedule;
import com.hb.pojo.Specialty;
import com.hb.repository.StatsRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private LocalSessionFactoryBean factory;

    @Override
    public List<Object[]> countPatientsByGender(LocalDate fromDate, LocalDate toDate) {

        Session session = this.factory.getObject().getCurrentSession();
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

        Session session = this.factory.getObject().getCurrentSession();
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

        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Appointment> rootApp = q.from(Appointment.class);

        Join<Appointment, Patient> joinPatient = rootApp.join("patientId");
        Join<Appointment, Schedule> joinSchedule = rootApp.join("scheduleId");

        Predicate p1 = b.between(joinSchedule.get("date"), fromDate, toDate);
        Predicate p2 = b.equal(rootApp.get("status"), "COMPLETED");

        q.where(b.and(p1, p2));

        Join<Schedule, Specialty> joinSpecialty = joinSchedule.join("specialtyId");

        q.multiselect(
                joinSpecialty.get("name"),
                b.countDistinct(joinPatient.get("id"))
        );
        q.groupBy(joinSpecialty.get("name"));
        return session.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> serviceUsageStats(LocalDate fromDate, LocalDate toDate) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<PaymentItem> rootItem = q.from(PaymentItem.class);
        Join<PaymentItem, Payment> joinPayment = rootItem.join("paymentId");

        java.time.LocalDateTime fromDateTime = fromDate.atStartOfDay();
        java.time.LocalDateTime toDateTime = toDate.atTime(23, 59, 59, 999999999);

        q.where(
                b.equal(joinPayment.get("status"), PaymentStatus.SUCCESS),
                b.between(joinPayment.get("paidAt"), fromDateTime, toDateTime)
        );

        q.multiselect(
                rootItem.get("itemType"),
                b.count(rootItem.get("id"))
        );

        q.groupBy(rootItem.get("itemType"));
        q.orderBy(b.desc(b.count(rootItem.get("id"))));

        return session.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> topDiseasesStats(LocalDate fromDate, LocalDate toDate, int limit) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Object[]> statsRevenueByYear(int year) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();

        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Payment> rootPayment = q.from(Payment.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add((b.equal(rootPayment.get("status"), PaymentStatus.SUCCESS)));
        Expression<Integer> yearExpr = b.function("YEAR", Integer.class, rootPayment.get("paidAt"));
        predicates.add(b.equal(yearExpr, year));

        q.where(predicates.toArray(new Predicate[0]));

        Expression<Integer> monthExpr = b.function("MONTH", Integer.class, rootPayment.get("paidAt"));

        q.multiselect(
                monthExpr,
                b.sum(rootPayment.get("totalAmount"))
        );

        q.groupBy(monthExpr);

        q.orderBy(b.asc(monthExpr));

        return session.createQuery(q).getResultList();

    }

    @Override
    public List<Object[]> revenueDetailsStats(LocalDate fromDate, LocalDate toDate) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Object[]> statsRevenueBySpecialty(int year, int month) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Payment> rootPayment = q.from(Payment.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add((b.equal(rootPayment.get("status"), PaymentStatus.SUCCESS)));
        if (year != 0) {
            Expression<Integer> yearExpr = b.function("YEAR", Integer.class, rootPayment.get("paidAt"));
            predicates.add(b.equal(yearExpr, year));
        }
        if (month != 0) {
            Expression<Integer> monthExpr = b.function("MONTH", Integer.class, rootPayment.get("paidAt"));
            predicates.add(b.equal(monthExpr, month));
        }

        Join<Payment, Appointment> joinAppointment = rootPayment.join("appointmentId");
        Join<Appointment, Schedule> joinSchedule = joinAppointment.join("scheduleId");
        Join<Schedule, Specialty> joinSpecialty = joinSchedule.join("specialtyId");

        q.where(predicates.toArray(new Predicate[0]));

        q.multiselect(
                joinSpecialty.get("name"),
                b.sum(rootPayment.get("totalAmount"))
        );

        q.groupBy(joinSpecialty.get("name"));

        return session.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> statsRevenueByType(int year, int month) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Payment> rootPayment = q.from(Payment.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add((b.equal(rootPayment.get("status"), PaymentStatus.SUCCESS)));
        if (year != 0) {
            Expression<Integer> yearExpr = b.function("YEAR", Integer.class, rootPayment.get("paidAt"));
            predicates.add(b.equal(yearExpr, year));
        }
        if (month != 0) {
            Expression<Integer> monthExpr = b.function("MONTH", Integer.class, rootPayment.get("paidAt"));
            predicates.add(b.equal(monthExpr, month));
        }
        Join<Payment, PaymentItem> joinFirstItem = rootPayment.join("paymentItemCollection", JoinType.LEFT);

        Subquery<Integer> minItemIdSubq = q.subquery(Integer.class);
        Root<PaymentItem> subRootItem = minItemIdSubq.from(PaymentItem.class);
        minItemIdSubq.select(b.min(subRootItem.get("id")));
        minItemIdSubq.where(b.equal(subRootItem.get("paymentId"), rootPayment));
        predicates.add(b.equal(joinFirstItem.get("id"), minItemIdSubq));
        q.where(predicates.toArray(new Predicate[0]));

        q.multiselect(
                joinFirstItem.get("itemType"), 
                b.sum(rootPayment.get("totalAmount"))
        );

        q.groupBy( 
                joinFirstItem.get("itemType")
        );
        
        return session.createQuery(q).getResultList();
    }

}
