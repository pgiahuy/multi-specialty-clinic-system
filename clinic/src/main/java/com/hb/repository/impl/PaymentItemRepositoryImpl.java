/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Appointment;
import com.hb.pojo.Payment;
import com.hb.pojo.PaymentItem;
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
    public List<PaymentItem> getItemsByPaymentId(Long paymentId) {
        Session session = this.factory.getObject().getCurrentSession();

        Query<PaymentItem> q = session.createQuery("FROM PaymentItem WHERE paymentId.id = :paymentId", PaymentItem.class);
        q.setParameter("paymentId", paymentId);
        return q.getResultList();
    }

    @Override
    public PaymentItem getItemById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(PaymentItem.class, id);
    }

    @Override
    public void addOrUpdateItem(PaymentItem item) {
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
        PaymentItem item = this.getItemById(id);
        if (item != null) {
            session.remove(item);
        }
    }

    @Override
    public PaymentItem getItemByAppointment(Long appointmentId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<PaymentItem> q = session.createQuery("FROM PaymentItem p WHERE p.referenceId = :appointmentId", PaymentItem.class);
        q.setParameter("appointmentId", appointmentId);

        return q.getSingleResult();
    }

    @Override
    public List<PaymentItem> getItemsByPayment(Payment payment) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "FROM PaymentItem p WHERE p.paymentId = :paymentId";
        Query<PaymentItem> query = session.createQuery(hql, PaymentItem.class);
        query.setParameter("paymentId", payment);

        return query.getResultList();
    }

    @Override
    public List<PaymentItem> getItemsByPaymentId(Long paymentId, Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("From PaymentItem p WHERE p.paymentId.id = :paymentId");

        String status = params.get("status");
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");

        if (status != null && status.isEmpty()) {
            hql.append("AND p.status = :status");
        }

        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            hql.append(" AND p.createdAt >= :startDate AND p.createdAt < :endDate");
        }

        Query<PaymentItem> query = session.createQuery(hql.toString(), PaymentItem.class);
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
    public List<PaymentItem> getPaymentItems(Map<String, String> params) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
