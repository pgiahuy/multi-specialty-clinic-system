/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Conversation;
import java.util.List;

/**
 *
 * @author HUY
 */
public interface ConversationRepository {
    Conversation getConversationById(Long id);
    Conversation save(Conversation c);
    List<Object[]> findAllConversationsWithPatientName(Long doctorId);
    List<Object[]> findAllConversationsWithDoctorName(Long patientId);
    Conversation findActiveQuickChat(Long doctorId, Long patientId);
    
}
