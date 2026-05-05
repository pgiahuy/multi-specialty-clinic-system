/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.PaymentItems;
import com.hb.repository.PaymentItemRepository;
import java.util.List;
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
public class PaymentItemRepositoryImpl implements PaymentItemRepository{
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
    
}
