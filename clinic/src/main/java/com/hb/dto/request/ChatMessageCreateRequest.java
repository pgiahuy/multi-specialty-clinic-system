/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/System/FileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @author HUY
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatMessageCreateRequest {
    private Long conversationId;
    private Long senderId;
    private String content;
    private String messageType;

    public ChatMessageCreateRequest() {
    }

    public ChatMessageCreateRequest(Long conversationId, Long senderId, String content, String messageType) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.messageType = messageType;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
