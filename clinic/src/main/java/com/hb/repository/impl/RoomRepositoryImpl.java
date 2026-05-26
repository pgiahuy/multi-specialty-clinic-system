/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Rooms;
import com.hb.repository.RoomRepository;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DELL
 */
@Repository
@Transactional
public class RoomRepositoryImpl extends BaseRepositoryImpl<Rooms> implements RoomRepository {


    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Rooms> getRooms(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Rooms> q = session.createNamedQuery("Rooms.findAll", Rooms.class);

        if (params != null) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
        }
        return q.getResultList();
    }

    @Override
    public Rooms getRoomById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Rooms.class, id);
    }

    @Override
    public Rooms saveOrUpdate(Rooms a) {
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

        Rooms r = session.get(Rooms.class, id);

        if (r != null) {
            session.remove(r);
        } else {
            throw new RuntimeException("Room not found!");
        }
    }

}
