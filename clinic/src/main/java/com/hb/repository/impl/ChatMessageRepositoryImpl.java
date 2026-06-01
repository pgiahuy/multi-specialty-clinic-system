package com.hb.repository.impl;

import com.hb.pojo.ChatMessage;
import com.hb.repository.ChatMessageRepository;
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
public class ChatMessageRepositoryImpl implements ChatMessageRepository {
    
    @Autowired  
    private LocalSessionFactoryBean factory;


    @Override
    public List<ChatMessage> findByConversationIdOrderByCreatedAtAsc(Long conversationId) {
        Session session = this.factory.getObject().getCurrentSession();
        
        String hql = "FROM ChatMessage m "
                   + "WHERE m.conversationId.id = :conversationId "
                   + "ORDER BY m.createdAt ASC";
        
        Query<ChatMessage> query = session.createQuery(hql, ChatMessage.class);
        query.setParameter("conversationId", conversationId);
        
        return query.getResultList();
    }

    @Override
    public ChatMessage save(ChatMessage msg) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(msg);
        return msg;
    }
}