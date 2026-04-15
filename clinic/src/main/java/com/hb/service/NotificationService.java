/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.Notification;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface NotificationService {
    Notification addNotification(Map<String, String> params);
    List<Notification> getNotificationsByUserId(Map<String, String> params);
    Notification getNotificationById(Long id);
    void deleteNotification(Long id);
}