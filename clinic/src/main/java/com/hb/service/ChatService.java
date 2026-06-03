/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.response.ChatMessageResponse;
import com.hb.pojo.Conversation;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface ChatService {
    List<Map<String, Object>> getConversationsForDoctor(Long doctorId);
    List<Map<String, Object>> getConversationsForPatient(Long patientId);
    List<ChatMessageResponse> getMessagesByConversation(Long conversationId);
    Conversation quickStartConversation(Long doctorId, Long patientId);
    ChatMessageResponse saveAndPushMessage(Long conversationId, Long senderId, String content, String msgType, String currentSenderType);
    
}
