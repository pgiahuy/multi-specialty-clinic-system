/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.response.ChatMessageResponse;

/**
 *
 * @author HUY
 */
public interface FcmService {
    void sendPushNotification(String token, String notiId, String title, String content, String path);
    void sendChatMessageToUserDevice(String token, ChatMessageResponse message);
}
