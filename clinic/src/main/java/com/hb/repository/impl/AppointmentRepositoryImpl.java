/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.enums.AppointmentStatus;
import com.hb.pojo.Appointment;
import com.hb.pojo.Patient;
import com.hb.pojo.Schedule;
import com.hb.pojo.Shift;
import com.hb.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
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

        Fetch<Appointment, Patient> patientFetch = root.fetch("patientId", JoinType.LEFT);
        Join<Appointment, Patient> patientJoin = (Join<Appointment, Patient>) patientFetch;

        Fetch<Appointment, Schedule> scheduleFetch = root.fetch("scheduleId", JoinType.LEFT);
        Join<Appointment, Schedule> scheduleJoin = (Join<Appointment, Schedule>) scheduleFetch;

        scheduleFetch.fetch("doctorId", JoinType.LEFT);
        scheduleFetch.fetch("shiftId", JoinType.LEFT);
        scheduleFetch.fetch("roomId", JoinType.LEFT);

        Join<Schedule, ?> doctorJoin = scheduleJoin.join("doctorId", JoinType.LEFT);

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

            if (params.containsKey("scheduleId") && !params.get("scheduleId").isEmpty()) {
                predicates.add(cb.equal(scheduleJoin.get("id"), Long.valueOf(params.get("scheduleId"))));
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
    public long countAppointments(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Appointment> root = cq.from(Appointment.class);
        cq.select(cb.countDistinct(root));

        Join<Appointment, Patient> patientJoin = root.join("patientId", JoinType.LEFT);
        Join<Appointment, Schedule> scheduleJoin = root.join("scheduleId", JoinType.LEFT);
        Join<Schedule, ?> doctorJoin = scheduleJoin.join("doctorId", JoinType.LEFT);

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

            if (params.containsKey("scheduleId") && !params.get("scheduleId").isEmpty()) {
                predicates.add(cb.equal(scheduleJoin.get("id"), Long.valueOf(params.get("scheduleId"))));
                if ("ROLE_DOCTOR".equals(params.get("currentUserRole"))) {
                    predicates.add(cb.notEqual(root.get("status"), AppointmentStatus.UN_PAID));
                }
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));

        Query<Long> q = session.createQuery(cq);
        Long result = q.getSingleResult();
        return result != null ? result : 0L;
    }

    @Override
    public long count(Map<String, String> params, Class<Appointment> clazz) {
        return countAppointments(params);
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
    public Appointment addOrUpdateAppointment(Appointment a) {
        Session session = this.factory.getObject().getCurrentSession();
        if (a.getId() == null) {
            session.persist(a);
            return a;
        } else {
            return session.merge(a);
        }
    }

    @Override
    public boolean isPatientAlreadyBookedInSchedule(Long patientId, Long scheduleId) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "SELECT count(a.id) FROM Appointment a WHERE a.patientId.id = :patientId "
                + "AND a.scheduleId.id = :scheduleId AND a.status != :status";

        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("patientId", patientId);
        query.setParameter("scheduleId", scheduleId);
        query.setParameter("status", AppointmentStatus.CANCELLED);

        Long count = query.uniqueResult();

        return count != null && count > 0;
    }

    @Override
    public List<Appointment> getConfirmedAppointmentsForReminder(LocalDateTime from, LocalDateTime to) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);

        root.fetch("patientId", JoinType.LEFT);
        Fetch<Appointment, Schedule> scheduleFetch = root.fetch("scheduleId", JoinType.LEFT);
        Join<Appointment, Schedule> scheduleJoin = (Join<Appointment, Schedule>) scheduleFetch;
        Join<Schedule, Shift> shiftJoin = scheduleJoin.join("shiftId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("status"), AppointmentStatus.CONFIRMED));
        predicates.add(cb.or(cb.isFalse(root.get("reminderSent")), cb.isNull(root.get("reminderSent"))));
        predicates.add(cb.equal(scheduleJoin.get("date"), from.toLocalDate()));
        predicates.add(cb.between(shiftJoin.get("startTime"), from.toLocalTime(), to.toLocalTime()));

        cq.select(root).distinct(true);
        cq.where(predicates.toArray(new Predicate[0]));

        Query<Appointment> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<Appointment> getAppointmentByPatientId(Long patientId, Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);

        root.fetch("patientId", JoinType.LEFT);

        Fetch<Appointment, Schedule> scheduleFetch = root.fetch("scheduleId", JoinType.LEFT);
        Join<Appointment, Schedule> scheduleJoin = (Join<Appointment, Schedule>) scheduleFetch;

        scheduleFetch.fetch("doctorId", JoinType.LEFT);
        scheduleFetch.fetch("shiftId", JoinType.LEFT);
        scheduleFetch.fetch("roomId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("patientId").get("id"), patientId));

        if (params != null) {
            String status = params.get("status");
            String startDate = params.get("startDate");
            String endDate = params.get("endDate");

            if (status != null && !status.isEmpty()) {

                predicates.add(cb.equal(root.get("status"), status));
            }

            if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate start = LocalDate.parse(startDate, formatter);
                LocalDate end = LocalDate.parse(endDate, formatter).plusDays(1);

                predicates.add(cb.greaterThanOrEqualTo(scheduleJoin.get("date"), start));
                predicates.add(cb.lessThan(scheduleJoin.get("date"), end));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(scheduleJoin.get("date")));

        Query<Appointment> query = session.createQuery(cq);
        return query.getResultList();
    }

}
