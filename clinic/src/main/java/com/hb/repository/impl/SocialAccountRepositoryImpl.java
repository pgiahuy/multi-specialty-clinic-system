/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.SocialAccount;
import com.hb.repository.SocialAccountRepository;
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
@Transactional
@Repository
public class SocialAccountRepositoryImpl implements SocialAccountRepository{

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public SocialAccount findByProviderAndProviderId(String provider, String providerId) {
        Session session = this.factory.getObject().getCurrentSession();
        
        String hql = "FROM SocialAccount WHERE provider = :provider AND providerId = :providerId";
        Query<SocialAccount> q = session.createQuery(hql, SocialAccount.class);
        q.setParameter("provider", provider);
        q.setParameter("providerId", providerId);
        
        return q.uniqueResultOptional().orElse(null);
    }

    @Override
    public void save(SocialAccount socialAccount) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(socialAccount);
    }
    
}
