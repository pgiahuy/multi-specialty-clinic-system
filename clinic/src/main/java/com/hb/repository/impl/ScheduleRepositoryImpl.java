/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Schedule;
import com.hb.repository.ScheduleRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.MutationQuery;
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
public class ScheduleRepositoryImpl extends BaseRepositoryImpl<Schedule> implements ScheduleRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Schedule> getSchedules(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule> root = cq.from(Schedule.class);
        cq.select(root).distinct(true);

        root.fetch("doctorId", JoinType.LEFT);
        root.fetch("shiftId", JoinType.LEFT);
        root.fetch("specialtyId", JoinType.LEFT);
        root.fetch("roomId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            if (params.containsKey("doctorId") && !params.get("doctorId").isEmpty()) {
                predicates.add(cb.equal(root.get("doctorId").get("id"), Long.valueOf(params.get("doctorId"))));
            }
            if (params.containsKey("specialtyId") && !params.get("specialtyId").isEmpty()) {
                predicates.add(cb.equal(root.get("specialtyId").get("id"), Long.valueOf(params.get("specialtyId"))));
            }
            if (params.containsKey("doctorName") && !params.get("doctorName").isEmpty()) {
                String pattern = "%" + params.get("doctorName").toLowerCase().trim() + "%";
                predicates.add(cb.like(cb.lower(root.get("doctorId").get("fullName")), pattern));
            }

            if (params.containsKey("specialtyName") && !params.get("specialtyName").isEmpty()) {
                String pattern = "%" + params.get("specialtyName").toLowerCase().trim() + "%";
                predicates.add(cb.like(cb.lower(root.get("specialtyId").get("name")), pattern));
            }

            boolean hasFromDate = params.containsKey("fromDate") && !params.get("fromDate").isEmpty();
            boolean hasToDate = params.containsKey("toDate") && !params.get("toDate").isEmpty();

            if (hasFromDate && hasToDate) {
                LocalDate from = LocalDate.parse(params.get("fromDate"));
                LocalDate to = LocalDate.parse(params.get("toDate"));
                predicates.add(cb.between(root.get("date"), from, to));
            } else if (hasFromDate) {
                LocalDate from = LocalDate.parse(params.get("fromDate"));
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), from));
            } else if (hasToDate) {
                LocalDate to = LocalDate.parse(params.get("toDate"));
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), to));
            }

            if (params.containsKey("date") && !params.get("date").isEmpty()) {
                LocalDate singleDate = LocalDate.parse(params.get("date"));
                predicates.add(cb.equal(root.get("date"), singleDate));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));

        Query<Schedule> q = session.createQuery(cq);

        if (params != null && params.containsKey("pageSize")) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            q.setMaxResults(pageSize);
            q.setFirstResult((page - 1) * pageSize);
        }

        return q.getResultList();
    }

    @Override
    public Schedule saveOrUpdate(Schedule s) {
        Session session = this.factory.getObject().getCurrentSession();

        if (s.getId() == null) {
            session.persist(s);
            return s;
        } else {
            return session.merge(s);
        }

    }

    @Override
    public Schedule getScheduleById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Schedule.class, id);
    }

    @Override
    public void deleteSchedule(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Schedule s = session.get(Schedule.class, id);
        if (s != null) {
            session.remove(s);
        }
    }

    @Override
    public boolean checkDoctorAvailability(Long doctorId, LocalDate date, Long shiftId, Long excludeId) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT COUNT(s) FROM Schedule s "
                + "WHERE s.doctorId.id = :doctorId "
                + "AND s.date = :date "
                + "AND s.shiftId.id = :shiftId ");

        if (excludeId != null) {
            hql.append(" AND s.id != :excludeId ");
        }

        Query<Long> q = session.createQuery(hql.toString(), Long.class);
        q.setParameter("doctorId", doctorId);
        q.setParameter("date", date);
        q.setParameter("shiftId", shiftId);
        if (excludeId != null) {
            q.setParameter("excludeId", excludeId);
        }
        return q.getSingleResult() == 0;
    }

    @Override
    public boolean checkRoomAvailability(Long roomId, LocalDate date, Long shiftId, Long excludeId) {
        Session session = this.factory.getObject().getCurrentSession();
        StringBuilder hql = new StringBuilder("SELECT COUNT(s) FROM Schedule s "
                + "WHERE s.roomId.id = :roomId "
                + "AND s.date = :date "
                + "AND s.shiftId.id = :shiftId ");

        if (excludeId != null) {
            hql.append(" AND s.id != :excludeId ");
        }
        Query<Long> q = session.createQuery(hql.toString(), Long.class);
        q.setParameter("roomId", roomId);
        q.setParameter("date", date);
        q.setParameter("shiftId", shiftId);
        if (excludeId != null) {
            q.setParameter("excludeId", excludeId);
        }

        return q.getSingleResult() == 0;
    }

    @Override
    public Schedule getScheduleByDoctor(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Schedule> q = session.createQuery("SELECT s FROM Schedule s WHERE s.id = :scheduleId "
                + "AND s.doctorId.id =: doctorId", Schedule.class);

        if (params != null && params.containsKey("scheduleId") && params.containsKey("doctorId")) {
            q.setParameter("scheduleId", params.get("scheduleId"));
            q.setParameter("doctorId", params.get("doctorId"));
        }

        return q.getSingleResult();
    }

    @Override
    public long count(Map<String, String> params, Class<Schedule> clazz) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Schedule> root = cq.from(Schedule.class);

        cq.select(cb.countDistinct(root));

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {

            if (params.containsKey("doctorId") && !params.get("doctorId").isEmpty()) {
                predicates.add(cb.equal(root.get("doctorId").get("id"), Long.valueOf(params.get("doctorId"))));
            }
            if (params.containsKey("specialtyId") && !params.get("specialtyId").isEmpty()) {
                predicates.add(cb.equal(root.get("specialtyId").get("id"), Long.valueOf(params.get("specialtyId"))));
            }
            if (params.containsKey("doctorName") && !params.get("doctorName").isEmpty()) {
                String pattern = "%" + params.get("doctorName").toLowerCase().trim() + "%";
                predicates.add(cb.like(cb.lower(root.get("doctorId").get("fullName")), pattern));
            }
            if (params.containsKey("specialtyName") && !params.get("specialtyName").isEmpty()) {
                String pattern = "%" + params.get("specialtyName").toLowerCase().trim() + "%";
                predicates.add(cb.like(cb.lower(root.get("specialtyId").get("name")), pattern));
            }

            boolean hasFromDate = params.containsKey("fromDate") && !params.get("fromDate").isEmpty();
            boolean hasToDate = params.containsKey("toDate") && !params.get("toDate").isEmpty();

            if (hasFromDate && hasToDate) {
                LocalDate from = LocalDate.parse(params.get("fromDate"));
                LocalDate to = LocalDate.parse(params.get("toDate"));
                predicates.add(cb.between(root.get("date"), from, to));
            } else if (hasFromDate) {
                LocalDate from = LocalDate.parse(params.get("fromDate"));
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), from));
            } else if (hasToDate) {
                LocalDate to = LocalDate.parse(params.get("toDate"));
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), to));
            }

            if (params.containsKey("date") && !params.get("date").isEmpty()) {
                LocalDate singleDate = LocalDate.parse(params.get("date"));
                predicates.add(cb.equal(root.get("date"), singleDate));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));

        Query<Long> q = session.createQuery(cq);
        Long result = q.getSingleResult();

        return result != null ? result : 0L;
    }

    @Override
    public int incrementCurrentPatients(Long scheduleId) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "UPDATE Schedule s SET s.currentPatients = s.currentPatients + 1 "
                + "WHERE s.id = :id AND s.currentPatients < s.maxPatients";
        MutationQuery query = session.createMutationQuery(hql);
        query.setParameter("id", scheduleId);

        return query.executeUpdate();
    }

    @Override
    public int decrementCurrentPatients(Long scheduleId) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "UPDATE Schedule s SET s.currentPatients = s.currentPatients - 1 "
                + "WHERE s.id = :id AND s.currentPatients > 0";

        MutationQuery query = session.createMutationQuery(hql);
        query.setParameter("id", scheduleId);

        return query.executeUpdate();
    }

}
