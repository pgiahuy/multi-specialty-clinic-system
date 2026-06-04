/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Patient;
import com.hb.pojo.User;
import com.hb.repository.SocialAccountRepository;
import com.hb.repository.UserRepository;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HUY
 */
@Repository
@Transactional
public class UserRepositoryImpl extends BaseRepositoryImpl<User> implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private SocialAccountRepository socialAccountRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("FROM User u WHERE 1=1");

        if (params != null && params.containsKey("kw")) {
            hql.append(" AND u.username LIKE :kw");
        }
        hql.append(" AND u.isActive=true");

        Query<User> q = session.createQuery(hql.toString(), User.class);

        if (params != null && params.containsKey("kw")) {
            q.setParameter("kw", "%" + params.get("kw") + "%");
        }

        if (params != null && params.containsKey("pageSize")) {
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            q.setMaxResults(pageSize);
            q.setFirstResult((page - 1) * pageSize);
        }

        return q.getResultList();
    }

    @Override
    public long count(Map<String, String> params, Class<User> clazz) {
        Session session = this.factory.getObject().getCurrentSession();

        StringBuilder hql = new StringBuilder("SELECT COUNT(u) FROM User u WHERE 1=1");

        if (params != null && params.containsKey("kw")) {
            hql.append(" AND u.username LIKE :kw");
        }
        hql.append(" AND u.isActive=true");

        Query<Long> q = session.createQuery(hql.toString(), Long.class);

        if (params != null && params.containsKey("kw")) {
            q.setParameter("kw", "%" + params.get("kw") + "%");
        }

        return q.getSingleResult();
    }

    @Override
    public User getUserById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        User user = session.get(User.class, id);
        return user;

    }

    @Override
    public User getUserByUsername(String username) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "SELECT u FROM User u LEFT JOIN FETCH u.patientCollection WHERE u.username = :username";
        Query<User> query = session.createQuery(hql, User.class);
        query.setParameter("username", username);
        return query.getSingleResult();

    }

    @Override
    public User saveOrUpdate(User u) {
        Session session = this.factory.getObject().getCurrentSession();
        if (u.getId() == null) {
            session.persist(u);
            return u;
        } else {
            return session.merge(u);
        }
    }

    @Override
    public void deleteUser(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        User u = session.get(User.class, id);

        if (u != null) {
            if (u.getPatientCollection() != null) {
                for (Patient p : u.getPatientCollection()) {
                    p.setUserId(null);
                }
            }

            if (u.getDoctor() != null) {
                u.getDoctor().setUserId(null);
            }
            u.setIsActive(false);
            session.merge(u);
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
        Query<User> query = session.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);

        List<User> users = query.getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User existsByUsername(String username) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<User> q = session.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);

        return q.uniqueResultOptional().orElse(null);
    }

    @Override
    public User existsByEmail(String email) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<User> q = session.createNamedQuery("User.findByEmail", User.class);
        q.setParameter("email", email);

        return q.uniqueResultOptional().orElse(null);
    }

    @Override
    public List<User> getActiveUsers(String kw) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "FROM User u WHERE u.isActive = true "
                + "AND NOT EXISTS (FROM Doctor d WHERE d.userId = u) "
                + "AND NOT EXISTS (FROM Patient p WHERE p.userId = u) "
                + "AND (:kw IS NULL OR :kw = '' OR u.username LIKE :kw OR u.name LIKE :kw)";

        Query<User> q = session.createQuery(hql, User.class);

        String searchKw = (kw != null && !kw.isEmpty()) ? "%" + kw + "%" : null;
        q.setParameter("kw", searchKw);

        return q.getResultList();
    }

}
