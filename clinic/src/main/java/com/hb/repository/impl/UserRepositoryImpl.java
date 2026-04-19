/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.User;
import com.hb.repository.UserRepository;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HUY
 */
@Repository
@Transactional
public class UserRepositoryImpl extends BaseRepositoryImpl<User> implements UserRepository  {


    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<User> q = session.createNamedQuery("User.findAll", User.class);

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
    public User getUserByUsername(String username) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<User> q = session.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        return q.getSingleResult();

    }

    @Override
    public User addUser(User u) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(u);
        return u;
    }

    @Override
    public void deleteUser(Long id) {
        Session session = this.factory.getObject().getCurrentSession();

        User u = session.get(User.class, id);

        if (u != null) {
            session.remove(u);
        } else {
            throw new RuntimeException("User not found!");
        }
    }

    @Override
    public boolean authenticate(String username, String password) {
        User u = this.getUserByUsername(username);

        return this.passwordEncoder.matches(password, u.getPassword());
    }

    @Override
    public User getUserByEmail(String email) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<User> q = session.createNamedQuery("User.findByEmail", User.class);
        q.setParameter("email", email);
        return q.getSingleResult();
    }
}
