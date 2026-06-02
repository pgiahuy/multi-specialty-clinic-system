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
public class NotificationRepositoryImpl implements NotificationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Notification> getNotificationsByUserId(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "SELECT n FROM Notification n WHERE n.userId.id = :userId ORDER BY n.createdAt DESC";
        Query<Notification> q = session.createQuery(hql, Notification.class);

        if (params != null && params.containsKey("userId")) {
            Long userId = Long.valueOf(params.get("userId"));
            q.setParameter("userId", userId);

            if (params.containsKey("pageSize")) {
                int pageSize = Integer.parseInt(params.get("pageSize"));
                int page = Integer.parseInt(params.getOrDefault("page", "1"));
                int start = (page - 1) * pageSize;

                q.setMaxResults(pageSize);
                q.setFirstResult(start);
            }
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
    public void deleteNotification(Notification n) {
        Session session = this.factory.getObject().getCurrentSession();
        session.remove(n);
    }

    @Override
    public void markAsRead(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        MutationQuery q = session.createMutationQuery("UPDATE Notification n SET n.isRead = true WHERE n.id = :id");
        q.setParameter("id", id);
        q.executeUpdate();
    }

    @Override
    public void markAllAsRead(String username) {
        Session session = this.factory.getObject().getCurrentSession();
        MutationQuery q = session.createMutationQuery("UPDATE Notification n SET n.isRead = true"
                + " WHERE n.userId.username = :username AND n.isRead = false");
        q.setParameter("username", username);
        q.executeUpdate();
    }
}
