package com.hb.dto.response;

import java.time.LocalDateTime;

public class ConversationResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long receiverId;
    private String receiverName;
    private Long appointmentId;
    private String conversationType;
    private Boolean isActive;
    private LocalDateTime createdAt;

    public ConversationResponse() {
    }

    public ConversationResponse(Long id, Long patientId, String patientName, Long receiverId, 
                               String receiverName, Long appointmentId, String conversationType, 
                               Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.appointmentId = appointmentId;
        this.conversationType = conversationType;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getConversationType() {
        return conversationType;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
