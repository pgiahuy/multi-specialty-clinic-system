/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Appointment;
import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItems;
import com.hb.repository.PaymentItemRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
 * @author DELL
 */
@Repository
@Transactional
public class PaymentItemRepositoryImpl implements PaymentItemRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<PaymentItems> getItemsByPaymentId(Long paymentId) {
        Session session = this.factory.getObject().getCurrentSession();

        Query<PaymentItems> q = session.createQuery("FROM PaymentItems WHERE paymentId.id = :paymentId", PaymentItems.class);
        q.setParameter("paymentId", paymentId);
        return q.getResultList();
    }

    @Override
    public PaymentItems getItemById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(PaymentItems.class, id);
    }

    @Override
    public void addOrUpdateItem(PaymentItems item) {
        Session session = this.factory.getObject().getCurrentSession();
        if (item.getId() != null) {
            session.merge(item);
        } else {
            session.persist(item);
        }
    }

    @Override
    public void deleteItem(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        PaymentItems item = this.getItemById(id);
        if (item != null) {
            session.remove(item);
        }
    }

    @Override
    public PaymentItems getItemByAppointment(Long appointmentId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<PaymentItems> q = session.createQuery("FROM PaymentItems p WHERE p.referenceId = :appointmentId", PaymentItems.class);
        q.setParameter("appointmentId", appointmentId);

        return q.getSingleResult();
    }

    @Override
    public List<PaymentItems> getItemsByPayment(Payment payment) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "FROM PaymentItems p WHERE p.payment = :payment";
        Query<PaymentItems> query = session.createQuery(hql, PaymentItems.class);
        query.setParameter("payment", payment);

        return query.getResultList();
    }

    @Override
    public List<PaymentItems> getItemsByPaymentId(Long paymentId, Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("From PaymentItems p WHERE p.payment.id = :paymentId");

        String status = params.get("status");
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");

        if (status != null && status.isEmpty()) {
            hql.append("AND p.status = :status");
        }

        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            hql.append(" AND p.createdAt >= :startDate AND p.createdAt < :endDate");
        }

        Query<PaymentItems> query = session.createQuery(hql.toString(), PaymentItems.class);
        query.setParameter("paymentId", paymentId);

        if (status != null && status.isEmpty()) {
            query.setParameter("status", status);
        }

        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            query.setParameter("startDate", start);
            query.setParameter("endDate", end.plusDays(1));
        }

        return query.getResultList();
    }

    @Override
    public List<PaymentItems> getPaymentItems(Map<String, String> params) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
