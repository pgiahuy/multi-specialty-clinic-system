/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Room;
import com.hb.repository.RoomRepository;
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
public class RoomRepositoryImpl extends BaseRepositoryImpl<Room> implements RoomRepository {


    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Room> getRooms(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        StringBuilder hql = new StringBuilder(
                "SELECT DISTINCT r FROM Room r LEFT JOIN FETCH r.areaId a WHERE 1=1");

        if (params != null && hasText(params.get("roomNumber"))) {
            hql.append(" AND r.roomNumber LIKE :roomNumber");
        }
        if (params != null && hasText(params.get("areaName"))) {
            hql.append(" AND a.areaName LIKE :areaName");
        }

        Query<Room> q = session.createQuery(hql.toString(), Room.class);

        if (params != null && hasText(params.get("roomNumber"))) {
            q.setParameter("roomNumber", "%" + params.get("roomNumber").trim() + "%");
        }
        if (params != null && hasText(params.get("areaName"))) {
            q.setParameter("areaName", "%" + params.get("areaName").trim() + "%");
        }

        if (params != null && params.containsKey("pageSize")) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            q.setFirstResult((page - 1) * pageSize);
            q.setMaxResults(pageSize);
        }
        return q.getResultList();
    }

    @Override
    public long count(Map<String, String> params, Class<Room> clazz) {
        Session session = this.factory.getObject().getCurrentSession();
        StringBuilder hql = new StringBuilder(
                "SELECT COUNT(DISTINCT r.id) FROM Room r LEFT JOIN r.areaId a WHERE 1=1");

        if (params != null && hasText(params.get("roomNumber"))) {
            hql.append(" AND r.roomNumber LIKE :roomNumber");
        }
        if (params != null && hasText(params.get("areaName"))) {
            hql.append(" AND a.areaName LIKE :areaName");
        }

        Query<Long> q = session.createQuery(hql.toString(), Long.class);

        if (params != null && hasText(params.get("roomNumber"))) {
            q.setParameter("roomNumber", "%" + params.get("roomNumber").trim() + "%");
        }
        if (params != null && hasText(params.get("areaName"))) {
            q.setParameter("areaName", "%" + params.get("areaName").trim() + "%");
        }

        return q.getSingleResult();
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    @Override
    public Room getRoomById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Room.class, id);
    }

    @Override
    public Room saveOrUpdate(Room a) {
        Session session = this.factory.getObject().getCurrentSession();
        if (a.getId()==null) {
            session.persist(a);
            return a;
        }else{
            return session.merge(a);
        }
    }

    @Override
    public void deleteRoom(Long id) {
        Session session = this.factory.getObject().getCurrentSession();

        Room r = session.get(Room.class, id);

        if (r != null) {
            session.remove(r);
        } else {
            throw new RuntimeException("Room not found!");
        }
    }

}
