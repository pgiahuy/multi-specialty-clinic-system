/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Payment;
import com.hb.pojo.PrescriptionItem;
import com.hb.repository.PaymentRepository;
import com.nimbusds.jose.Payload;
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
public class PaymentRepositoryImpl implements PaymentRepository {

  

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Payment> getPaymentsByUserName(Map<String, String> params) {

        String userName = params.get("username");

        Session session = this.factory.getObject().getCurrentSession();

        return session.createQuery(
                "SELECT p FROM Payment p JOIN p.appointmentId a JOIN a.patient pt JOIN pt.userId u "
                + "WHERE u.username = :username",
                Payment.class
        )
                .setParameter("username", userName)
                .getResultList();
    }

    @Override
    public List<Payment> getPayments(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Payment> q = session.createNamedQuery("Payment.findAll", Payment.class);

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
}
