package com.hb.mapper;

import com.hb.dto.response.ChatMessageResponse;
import com.hb.pojo.ChatMessage;
import org.springframework.stereotype.Component;

/**
 * @author HUY
 */
@Component
public class ChatMessageMapper {
    
    public ChatMessageResponse toResponse(ChatMessage msg) {
        if (msg == null) {
            return null;
        }

        ChatMessageResponse res = new ChatMessageResponse();
        res.setId(msg.getId());
        res.setContent(msg.getContent());
        res.setMessageType(msg.getMessageType());
        res.setSenderType(msg.getSenderType());
        res.setCreatedAt(msg.getCreatedAt());
        
        if (msg.getConversationId() != null) {
            res.setConversationId(msg.getConversationId().getId());
        }
        
        if (msg.getSenderId() != null) {
            res.setSenderId(msg.getSenderId().getId());
            res.setSenderName(msg.getSenderId().getUsername());
        }

        return res;
    }
}
