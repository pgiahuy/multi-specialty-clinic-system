/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.hb.pojo.ChatMessage;
import com.hb.service.FcmService;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */
@Service
public class FcmServiceImpl implements FcmService {

    @Async("taskExecutor")
    @Override
    public void sendPushNotification(String token, String notiId, String title, String content, String path) {
        if (token == null || token.isEmpty()) {
            return;
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(content)
                .build();

        Message.Builder messageBuilder = Message.builder()
                .setToken(token)
                .setNotification(notification);

        if (path != null && !path.isEmpty()) {
            messageBuilder.putData("path", path);
        }
        messageBuilder.putData("id", notiId);

        Message message = messageBuilder.build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Firebase: Gửi thành công! ID: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Firebase Error: " + e.getMessage());
        }
    }

    @Async("taskExecutor")
    @Override
    public void sendChatMessageToUserDevice(String token, ChatMessage message) {
        try {

            if (token == null || token.isEmpty()) {
                return;
            }
            Map<String, String> dataPayload = new HashMap<>();
            dataPayload.put("type", "CHAT");
            dataPayload.put("message_id", String.valueOf(message.getId()));
            dataPayload.put("conversation_id", String.valueOf(message.getConversationId().getId()));
            dataPayload.put("sender_id", String.valueOf(message.getSenderId().getId()));
            dataPayload.put("sender_type", message.getSenderType());
            dataPayload.put("message_type", message.getMessageType());
            dataPayload.put("content", message.getContent());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dataPayload.put("created_at", message.getCreatedAt().format(formatter));

            Message fcmMessage = Message.builder()
                    .setToken(token)
                    .putAllData(dataPayload)
                    .build();

            String response = FirebaseMessaging.getInstance().send(fcmMessage);
            System.out.println("Firebase gửi tin nhắn chat thành công! ID: " + response);

        } catch (FirebaseMessagingException e) {
            System.err.println("Lỗi kết nối Firebase Admin SDK: " + e.getMessage());
        }
    }

}
