package com.hb.service.impl;

import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.ChatMessage;
import com.hb.pojo.Conversation;
import com.hb.pojo.Doctor;
import com.hb.pojo.Patient;
import com.hb.pojo.User;
import com.hb.repository.ChatMessageRepository;
import com.hb.repository.ConversationRepository;
import com.hb.repository.PatientRepository;
import com.hb.repository.UserRepository;
import com.hb.service.ChatService;
import com.hb.service.FcmService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ConversationRepository conversationRepo;

    @Autowired
    private ChatMessageRepository messageRepo;

    @Autowired
    private FcmService fcmService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Override
    public List<Map<String, Object>> getConversationsForDoctor(Long userId) {
        List<Object[]> rawData = conversationRepo.findAllConversationsWithPatientName(userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : rawData) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(row[0]));
            map.put("patient_id", String.valueOf(row[1]));
            map.put("receiver_id", String.valueOf(row[2]));
            map.put("appointment_id", row[3] != null ? String.valueOf(row[3]) : null);
            map.put("is_active", row[4]);
            map.put("patient_name", String.valueOf(row[5]));
            result.add(map);
        }
        return result;
    }

    @Override
    public List<ChatMessage> getMessagesByConversation(Long conversationId) {
        return messageRepo.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    @Override
    public List<Map<String, Object>> getConversationsForPatient(Long patientId) {
        List<Object[]> rawData = conversationRepo.findAllConversationsWithDoctorName(patientId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : rawData) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(row[0]));
            map.put("patient_id", String.valueOf(row[1]));
            map.put("receiver_id", String.valueOf(row[2]));
            map.put("appointment_id", row[3] != null ? String.valueOf(row[3]) : null);
            map.put("is_active", row[4]);
            map.put("doctor_name", row[5] != null ? String.valueOf(row[5]) : null);
            result.add(map);
        }
        return result;
    }

    @Override
    @Transactional
    public Conversation quickStartConversation(Long userId, Long patientId) {
        Conversation existingChat = conversationRepo.findActiveQuickChat(userId, patientId);

        if (existingChat != null) {
            return existingChat;
        }

        Conversation newConvo = new Conversation();
        newConvo.setConversationType("TUVAN");
        newConvo.setAppointmentId(null);
        newConvo.setIsActive(true);
        newConvo.setCreatedAt(LocalDateTime.now());

        Patient patientProxy = patientRepo.getPatientById(patientId);
        User userDoctor = userRepo.getUserById(userId);

        newConvo.setPatientId(patientProxy);
        newConvo.setReceiverId(userDoctor);

        return conversationRepo.save(newConvo);
    }

    @Override
    @Transactional
    public ChatMessage saveAndPushMessage(Long conversationId, Long senderId, String content, String msgType, String currentSenderType) {

        Conversation convo = conversationRepo.getConversationById(conversationId);
        if (convo == null) {
            throw new ResourceNotFoundException("Hội thoại không tồn tại ID: " + conversationId);
        }

        ChatMessage msg = new ChatMessage();
        msg.setSenderType(currentSenderType);
        msg.setMessageType(msgType != null ? msgType : "TEXT");
        msg.setContent(content);
        msg.setCreatedAt( LocalDateTime.now());
        msg.setConversationId(convo);

        User user = userRepo.getUserById(senderId);
        msg.setSenderId(user);

        ChatMessage savedMsg = messageRepo.save(msg);

        try {
            String targetFcmToken = null;
            String targetName = "";

            if ("DOCTOR".equals(currentSenderType)) {
                Patient currentPatient = convo.getPatientId();
                User targetUser = currentPatient.getUserId();
                targetFcmToken = targetUser.getFcmToken();
                targetName = currentPatient.getFullName();
            } else {
                User doctorUser = convo.getReceiverId();
                if (doctorUser != null) {
                    targetFcmToken = doctorUser.getFcmToken();
                    targetName = doctorUser.getDoctor().getFullName();
                }
            }

            if (targetFcmToken != null && !targetFcmToken.isEmpty()) {
                fcmService.sendChatMessageToUserDevice(targetFcmToken, savedMsg);
            } else {
                System.out.println("Người nhận " + targetName + " đang không online, không bắn FCM.");
            }

        } catch (Exception e) {
            System.err.println("Lỗi xử lý luồng Token FCM: " + e.getMessage());
        }

        return savedMsg;
    }
}
