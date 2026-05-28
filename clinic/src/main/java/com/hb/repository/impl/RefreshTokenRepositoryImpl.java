/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.User;
import com.hb.repository.RefreshTokenRepository;
import org.hibernate.Session;
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
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void deleteByUser(User u) {
        Session session = this.factory.getObject().getCurrentSession();
        
        session.update(u);
    }
    
}
