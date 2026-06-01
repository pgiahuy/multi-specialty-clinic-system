/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Schedule;
import com.hb.repository.ScheduleRepository;
import java.time.LocalDate;
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
public class ScheduleRepositoryImpl extends BaseRepositoryImpl<Schedule> implements ScheduleRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Schedule> getSchedules(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT DISTINCT s FROM Schedule s "
                + "LEFT JOIN FETCH s.doctorId "
                + "LEFT JOIN FETCH s.shiftId "
                + "LEFT JOIN FETCH s.specialtyId "
                + "LEFT JOIN FETCH s.roomId WHERE 1=1 ");

        if (params != null) {
            if (params.containsKey("doctorId") && !params.get("doctorId").isEmpty()) {
                hql.append(" AND s.doctorId.id = :docId ");
            }
            if (params.containsKey("specialtyId") && !params.get("specialtyId").isEmpty()) {
                hql.append(" AND s.specialtyId.id = :specId ");
            }
            if (params.containsKey("doctorName") && !params.get("doctorName").isEmpty()) {
                hql.append(" AND lower(s.doctorId.fullName) LIKE :doctorName ");
            }
            if (params.containsKey("specialtyName") && !params.get("specialtyName").isEmpty()) {
                hql.append(" AND lower(s.specialtyId.name) LIKE :specialtyName ");
            }
            boolean hasFromDate = params.containsKey("fromDate") && !params.get("fromDate").isEmpty();
            boolean hasToDate = params.containsKey("toDate") && !params.get("toDate").isEmpty();
            
            if (hasFromDate && hasToDate) {
                hql.append(" AND s.date BETWEEN :fromDate AND :toDate");
            } else if (hasFromDate) {
                hql.append(" AND s.date >= :fromDate");
            } else if (hasToDate) {
                hql.append(" AND s.date <= :toDate");
            }
            if (params.containsKey("date") && !params.get("date").isEmpty()) {
                hql.append(" AND s.date = :date");
            }
            
            
        }

        Query<Schedule> q = session.createQuery(hql.toString(), Schedule.class);

        if (params != null) {
            if (params.containsKey("doctorId") && !params.get("doctorId").isEmpty()) {
                q.setParameter("docId", Long.valueOf(params.get("doctorId")));
            }
            if (params.containsKey("specialtyId") && !params.get("specialtyId").isEmpty()) {
                q.setParameter("specId", Long.valueOf(params.get("specialtyId")));
            }
            if (params.containsKey("doctorName") && !params.get("doctorName").isEmpty()) {
                  q.setParameter("doctorName", "%" + params.get("doctorName").toLowerCase().trim() + "%");
            }
            if (params.containsKey("specialtyName") && !params.get("specialtyName").isEmpty()) {
                  q.setParameter("specialtyName", "%" + params.get("specialtyName").toLowerCase().trim() + "%");
            }
            
            boolean hasFromDate = params.containsKey("fromDate") && !params.get("fromDate").isEmpty();
            boolean hasToDate = params.containsKey("toDate") && !params.get("toDate").isEmpty();

            if (hasFromDate && hasToDate) {
                q.setParameter("fromDate", LocalDate.parse(params.get("fromDate")));
                q.setParameter("toDate", LocalDate.parse(params.get("toDate")));
            } else if (hasFromDate) {
                q.setParameter("fromDate", LocalDate.parse(params.get("fromDate")));
            } else if (hasToDate) {
                q.setParameter("toDate", LocalDate.parse(params.get("toDate")));
            }
            if (params.containsKey("date") && !params.get("date").isEmpty()) {
                q.setParameter("date", LocalDate.parse(params.get("date")));
            }
            

            if (params.containsKey("pageSize")) {
                int pageSize = Integer.parseInt(params.get("pageSize"));
                int page = Integer.parseInt(params.getOrDefault("page", "1"));
                q.setMaxResults(pageSize);
                q.setFirstResult((page - 1) * pageSize);
            }
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
    public long count(Map<String, String> params, Class<Schedule> clazz) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT COUNT(DISTINCT s) FROM Schedule s WHERE 1=1 ");

        if (params != null) {
            if (params.containsKey("doctorId") && !params.get("doctorId").isEmpty()) {
                hql.append(" AND s.doctorId.id = :docId ");
            }
            if (params.containsKey("specialtyId") && !params.get("specialtyId").isEmpty()) {
                hql.append(" AND s.specialtyId.id = :specId ");
            }
            if (params.containsKey("doctorName") && !params.get("doctorName").isEmpty()) {
                hql.append(" AND lower(s.doctorId.fullName) LIKE :doctorName ");
            }
            if (params.containsKey("specialtyName") && !params.get("specialtyName").isEmpty()) {
                hql.append(" AND lower(s.specialtyId.name) LIKE :specialtyName ");
            }
            boolean hasFromDate = params.containsKey("fromDate") && !params.get("fromDate").isEmpty();
            boolean hasToDate = params.containsKey("toDate") && !params.get("toDate").isEmpty();

            if (hasFromDate && hasToDate) {
                hql.append(" AND s.date BETWEEN :fromDate AND :toDate");
            } else if (hasFromDate) {
                hql.append(" AND s.date >= :fromDate");
            } else if (hasToDate) {
                hql.append(" AND s.date <= :toDate");
            }
            if (params.containsKey("date") && !params.get("date").isEmpty()) {
                hql.append(" AND s.date = :date");
            }
        }

        Query<Long> q = session.createQuery(hql.toString(), Long.class);

        if (params != null) {
            if (params.containsKey("doctorId") && !params.get("doctorId").isEmpty()) {
                q.setParameter("docId", Long.parseLong(params.get("doctorId")));
            }
            if (params.containsKey("specialtyId") && !params.get("specialtyId").isEmpty()) {
                q.setParameter("specId", Long.parseLong(params.get("specialtyId")));
            }
            if (params.containsKey("doctorName") && !params.get("doctorName").isEmpty()) {
                q.setParameter("doctorName", "%" + params.get("doctorName").toLowerCase().trim() + "%");
            }
            if (params.containsKey("specialtyName") && !params.get("specialtyName").isEmpty()) {
                q.setParameter("specialtyName", "%" + params.get("specialtyName").toLowerCase().trim() + "%");
            }
            boolean hasFromDate = params.containsKey("fromDate") && !params.get("fromDate").isEmpty();
            boolean hasToDate = params.containsKey("toDate") && !params.get("toDate").isEmpty();

            if (hasFromDate && hasToDate) {
                q.setParameter("fromDate", java.time.LocalDate.parse(params.get("fromDate")));
                q.setParameter("toDate", java.time.LocalDate.parse(params.get("toDate")));
            } else if (hasFromDate) {
                q.setParameter("fromDate", java.time.LocalDate.parse(params.get("fromDate")));
            } else if (hasToDate) {
                q.setParameter("toDate", java.time.LocalDate.parse(params.get("toDate")));
            }
            if (params.containsKey("date") && !params.get("date").isEmpty()) {
                q.setParameter("date", java.time.LocalDate.parse(params.get("date")));
            }
        }

        return q.getSingleResult();
    }
    
    

  

}
