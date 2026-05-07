/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Schedules;
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
public class ScheduleRepositoryImpl extends BaseRepositoryImpl<Schedules> implements ScheduleRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Schedules> getSchedules(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT DISTINCT s FROM Schedules s "
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
            if (params.containsKey("date") && !params.get("date").isEmpty()) {
                hql.append(" AND s.date = :date ");
            }
        }

        Query<Schedules> q = session.createQuery(hql.toString(), Schedules.class);

        if (params != null) {
            if (params.containsKey("doctorId") && !params.get("doctorId").isEmpty()) {
                q.setParameter("docId", Long.parseLong(params.get("doctorId")));
            }
            if (params.containsKey("specialtyId") && !params.get("specialtyId").isEmpty()) {
                q.setParameter("specId", Long.parseLong(params.get("specialtyId")));
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
    public Schedules addSchedule(Schedules s) {
        Session session = this.factory.getObject().getCurrentSession();

        if (s.getId() == null) {
            session.persist(s);
        } else {
            session.merge(s);
        }

        return s;
    }

    @Override
    public Schedules getScheduleById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Schedules.class, id);
    }

    @Override
    public void deleteSchedule(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Schedules s = session.get(Schedules.class, id);
        if (s != null) {
            session.remove(s);
        }
    }

}
