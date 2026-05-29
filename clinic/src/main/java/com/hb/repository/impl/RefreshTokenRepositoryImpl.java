/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.exception.DuplicateResourceException;
import com.hb.pojo.RefreshToken;
import com.hb.repository.RefreshTokenRepository;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
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
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public RefreshToken getByToken(String token) {

        Session session = this.factory.getObject().getCurrentSession();
        RefreshToken rt = session
                .createNamedQuery("RefreshToken.findByToken", RefreshToken.class)
                .setParameter("token", token).uniqueResult();

        return rt;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(refreshToken);
        return refreshToken;

    }

    @Override
    public void revokeByToken(String token) {

        Session session = this.factory.getObject().getCurrentSession();

        String hql = "UPDATE RefreshToken r SET r.revoked = true WHERE r.token = :token";

        session.createMutationQuery(hql)
                .setParameter("token", token)
                .executeUpdate();
    }

    @Override
    public void revokeAllByUser(Long userId) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "UPDATE RefreshToken r SET r.revoked = true WHERE r.userId.id = :userId";

        session.createMutationQuery(hql)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public void revokeByUserAndDevice(Long userId, String deviceId) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "UPDATE RefreshToken r SET r.revoked = true WHERE r.userId.id = :userId AND r.deviceId = :deviceId";

        session.createMutationQuery(hql)
                .setParameter("userId", userId)
                .setParameter("deviceId", deviceId)
                .executeUpdate();
    }

    @Override
    public Boolean tokenIsExist(String token) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.token = :token";

        Long count = session.createQuery(hql, Long.class)
                .setParameter("token", token)
                .getSingleResult();

        return count > 0;
    }

}
