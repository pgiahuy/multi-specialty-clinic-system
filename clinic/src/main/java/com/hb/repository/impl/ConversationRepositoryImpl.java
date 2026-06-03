package com.hb.repository.impl;

import com.hb.pojo.Conversation;
import com.hb.repository.ConversationRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HUY
 */
@Transactional
@Repository
public class ConversationRepositoryImpl implements ConversationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Conversation getConversationById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Conversation.class, id);
    }


    @Override
    public List<Object[]> findAllConversationsWithPatientName(Long doctorId) {
        Session session = this.factory.getObject().getCurrentSession();
        
        String hql = "SELECT c.id, c.patientId.id, c.receiverId.id, "
               + "(SELECT max(a.id) FROM Appointment a WHERE a.conversationId.id = c.id), "
               + "c.isActive, c.patientId.fullName "
               + "FROM Conversation c "
               + "JOIN c.patientId p "
               + "WHERE c.receiverId.id = :doctorId AND c.isActive = true "
               + "ORDER BY c.createdAt DESC";
        
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("doctorId", doctorId);
        
        return query.getResultList();
    }

    @Override
    public List<Object[]> findAllConversationsWithDoctorName(Long patientId) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = "SELECT c.id, c.patientId.id, c.receiverId.id, "
               + "(SELECT max(a.id) FROM Appointment a WHERE a.conversationId.id = c.id), "
               + "c.isActive, r.doctor.id, r.doctor.fullName "
               + "FROM Conversation c "
               + "JOIN c.receiverId r "
               + "WHERE c.patientId.id = :patientId AND c.isActive = true "
               + "ORDER BY c.createdAt DESC";

        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("patientId", patientId);

        return query.getResultList();
    }


    @Override
    public Conversation findActiveQuickChat(Long doctorId, Long patientId) {
        Session session = this.factory.getObject().getCurrentSession();
        
        String hql = "FROM Conversation c "
                   + "WHERE c.receiverId.id = :doctorId "
                   + "AND c.patientId.id = :patientId "
                   + "AND c.conversationType = 'TUVAN' "
                   + "AND c.isActive = true";
        
        Query<Conversation> query = session.createQuery(hql, Conversation.class);
        query.setParameter("doctorId", doctorId);
        query.setParameter("patientId", patientId);
        
        List<Conversation> results = query.getResultList();
        if (!results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public Conversation save(Conversation c) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(c);
        return c;
    }
}