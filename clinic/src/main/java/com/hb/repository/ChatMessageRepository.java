/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.ChatMessage;
import java.util.List;

/**
 *
 * @author HUY
 */
public interface ChatMessageRepository {
    ChatMessage save(ChatMessage msg);
    List<ChatMessage> findByConversationIdOrderByCreatedAtAsc(Long conversationId);
    
}
