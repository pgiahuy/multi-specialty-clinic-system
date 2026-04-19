/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.repository.BaseRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HUY
 */

@Repository
public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {
    
    @Autowired
    protected LocalSessionFactoryBean factory;

    @Override
    @Transactional
    public long count(Map<String, String> params, Class<T> clazz) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {

            params.forEach((key, value) -> {
            if (!key.equalsIgnoreCase("page") && !key.equalsIgnoreCase("pageSize")) {
                if (value != null && !value.isEmpty()) {
                    predicates.add(cb.like(root.get(key).as(String.class), String.format("%%%s%%", value)));
                }
            }
        });
        }

        cq.select(cb.count(root)).where((jakarta.persistence.criteria.Predicate[]) predicates.toArray(new Predicate[0]));
        return session.createQuery(cq).getSingleResult();
    }
}