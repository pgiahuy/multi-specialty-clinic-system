/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Notification;
import com.hb.repository.NotificationRepository;
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
 * @author HUY
 */
@Repository
@Transactional
@PropertySource("classpath:configs.properties")
public class NotificationRepositoryImpl implements NotificationRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Notification> getNotificationsByUserId(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Notification> q = session.createNamedQuery("Notification.findByUserId", Notification.class);

        if (params != null) {
            Long userId = Long.valueOf(params.get("userId"));
            q.setParameter("userId", userId);

            int pageSize = this.env.getProperty("notifications.page_size", Integer.class, 1);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
        }

        return q.getResultList();
    }

    @Override
    public Notification addNotification(Notification n) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(n);
        return n;
    }

    @Override
    public Notification getNotificationById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Notification.class, id);
    }

    @Override
    public void deleteNotification(Long id) {
        Session session = this.factory.getObject().getCurrentSession();

        Notification n = session.get(Notification.class, id);

        if (n != null) {
            session.remove(n);
        } else {
            throw new RuntimeException("Notification not found!");
        }
    }
}