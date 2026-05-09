/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */
@Service
public class FcmService {

    @Async("taskExecutor")
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
        } catch (Exception e) {
            System.err.println("Firebase Error: " + e.getMessage());
        }
    }
}