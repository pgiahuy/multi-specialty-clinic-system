/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.enums.PaymentMethod;
import com.hb.enums.PaymentStatus;
import com.hb.pojo.Payment;
import com.hb.repository.PaymentRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class PaymentRepositoryImpl implements PaymentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Payment> getPaymentsByUserName(Map<String, String> params) {

        String userName = params.get("username");

        Session session = this.factory.getObject().getCurrentSession();

        return session.createQuery(
                "SELECT p FROM Payment p JOIN p.appointmentId a JOIN a.patientId pt JOIN pt.userId u "
                + "WHERE u.username = :username",
                Payment.class
        )
                .setParameter("username", userName)
                .getResultList();
    }

    @Override
    public List<Payment> getPayments(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        StringBuilder hql = new StringBuilder("SELECT p FROM Payment p "
                 + "WHERE 1=1");
//                + "JOIN FETCH p.appointment a "
//                + "JOIN FETCH a.patientId WHERE 1=1");
//+ "JOIN FETCH p.paymentItemCollection i "

        if (params != null && params.containsKey("patientId")) {
            hql.append(" AND p.appointmentId.patientId.id = :patientId");
        }
        if (params != null && params.containsKey("endDate") && params.containsKey("startDate")) {
            hql.append(" AND p.createdAt >= :startDate AND p.createdAt < :endDate");
        } else if (params != null && params.containsKey("startDate")) {
            hql.append(" AND p.createdAt >= :startDate");
        } else if (params != null && params.containsKey("endDate")) {
            hql.append(" AND p.createdAt < :endDate");
        }

        hql.append(" ORDER BY p.createdAt DESC");

        Query<Payment> q = session.createQuery(hql.toString(), Payment.class);

        if (params != null && params.containsKey("patientId")) {
            q.setParameter("patientId", Long.valueOf(params.get("patientId")));
        }
        if (params != null && params.containsKey("startDate")) {
            q.setParameter("startDate", LocalDate.parse(params.get("startDate")).atStartOfDay());
        }
        if (params != null && params.containsKey("endDate")) {
            q.setParameter("endDate", LocalDate.parse(params.get("endDate")).plusDays(1).atStartOfDay());
        }

        if (params != null && params.containsKey("pageSize")) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
        }

        return q.getResultList();
    }

    @Override
    public Payment addPayment(Payment p) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(p);
        return p;
    }

    @Override
    public Payment getPaymentById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Payment.class, id);
    }

    @Override
    public void deletePayment(Long id) {
        Session session = this.factory.getObject().getCurrentSession();

        Payment p = session.get(Payment.class, id);

        if (p != null) {
            session.remove(p);
        } else {
            throw new RuntimeException("Payment not found!");
        }
    }

    @Override
    public void updatePayment(Payment p) {
        Session session = this.factory.getObject().getCurrentSession();
        session.merge(p);
    }

    @Override
    public Payment getPaymentByOrderId(String orderId) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.createQuery(
                "FROM Payment p WHERE p.orderId = :orderId", Payment.class)
                .setParameter("orderId", orderId)
                .uniqueResult();
    }

    @Override
    public void updatePaymentStatus(Long paymentId, PaymentStatus status, PaymentMethod method) {
        Session s = this.factory.getObject().getCurrentSession();
        Payment p = this.getPaymentById(paymentId);
        if (p != null) {
            p.setStatus(status);
            p.setMethod(method);
            s.merge(p);
        }
    }

    @Override
    public Payment addOrUpdatePayment(Payment p) {
        Session s = this.factory.getObject().getCurrentSession();
        if (p.getId() != null && p.getId() > 0) {
            return s.merge(p);
        } else {
            s.persist(p);
            return p;
        }
    }

//    @Override
//    public Payment getPaymentByItemId(Long Id) {
//        Session session = this.factory.getObject().getCurrentSession();
//        
//        Query<Payment> q = session.createQuery("FROM Payment p WHERE p.id = :id", Payment.class);
//        q.setParameter("id", Id);
//        
//        
//    }
    @Override
    public List<Payment> getPaymentByPatientId(Long patientId, Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder(
                "SELECT DISTINCT p FROM Payment p "
                + "LEFT JOIN FETCH p.paymentItem "
                + "WHERE p.appointment.patientId.id = :patientId"
        );
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");

        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            hql.append(" AND p.createdAt >= :startDate AND p.createdAt < :endDate");
        }

        hql.append(" ORDER BY p.createdAt DESC");

        Query<Payment> query = session.createQuery(hql.toString(), Payment.class);

        query.setParameter("patientId", patientId);

        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            query.setParameter("startDate", start.atStartOfDay());
            query.setParameter("endDate", end.plusDays(1).atStartOfDay());
        }

        return query.getResultList();
    }

}
