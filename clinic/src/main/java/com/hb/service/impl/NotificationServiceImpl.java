/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;


import com.hb.pojo.Notification;
import com.hb.pojo.User;
import com.hb.repository.NotificationRepository;
import com.hb.repository.UserRepository;
import com.hb.service.FcmService;
import com.hb.service.NotificationService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    
    @Autowired
    private FcmService fcmService;

    @Override
    public Notification addNotification(Map<String, String> params) {
        Notification n = new Notification();
        String title = params.getOrDefault("title", "Thông báo mới");
        String content = params.getOrDefault("content", "");
        String userName = params.get("username");
        String path = params.get("path");

        n.setTitle(title);
        n.setContent(content);
        n.setIsRead(false);
        n.setCreatedAt(new Date());

        if (userName != null && !userName.isEmpty()) {
            User user = userRepo.getUserByUsername(userName);
            n.setUserId(user);
            
            Notification savedNoti = this.notificationRepo.addNotification(n);
            
            if (user.getFcmToken() != null) {
                fcmService.sendPushNotification(user.getFcmToken(), title, content, path);
            }
            
            return savedNoti;
        }

        return this.notificationRepo.addNotification(n);
    }


    @Override
    public List<Notification> getNotificationsByUserId(Map<String, String> params) {
        return this.notificationRepo.getNotificationsByUserId(params);
    }

    @Override
    public Notification getNotificationById(Long id) {
        return this.notificationRepo.getNotificationById(id);
    }

    @Override
    public void deleteNotification(Long id) {
        this.notificationRepo.deleteNotification(id);
    }
}