/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Notification;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface NotificationRepository {
    List<Notification> getNotificationsByUserId(Map<String,String> params);
    Notification addNotification(Notification n);
    Notification getNotificationById(Long id);
    void deleteNotification(Long id);
    void markAsRead(Long id);
}
