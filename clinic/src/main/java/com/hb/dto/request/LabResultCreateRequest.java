/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;

import com.hb.enums.LabResultStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author DELL
 */
public class LabResultCreateRequest {
    private Long id;
    private Long appointmentId;
    private LocalDateTime createdAt;
    private LocalDateTime testAt;
    private LabResultStatus status;
    private List<LabResultDetailRequest> details;
    

    public LabResultCreateRequest() {
    }

    public LabResultCreateRequest(Long id, Long appointmentId, LocalDateTime createdAt, LocalDateTime testAt, LabResultStatus status, List<LabResultDetailRequest> details) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.createdAt = createdAt;
        this.testAt = testAt;
        this.status = status;
        this.details = details;
    }

   

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the createdAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the testAt
     */
    public LocalDateTime getTestAt() {
        return testAt;
    }

    /**
     * @param testAt the testAt to set
     */
    public void setTestAt(LocalDateTime testAt) {
        this.testAt = testAt;
    }

    /**
     * @return the details
     */
    public List<LabResultDetailRequest> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(List<LabResultDetailRequest> details) {
        this.details = details;
    }

    /**
     * @return the appointmentId
     */
    public Long getAppointmentId() {
        return appointmentId;
    }

    /**
     * @param appointmentId the appointmentId to set
     */
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * @return the status
     */
    public LabResultStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(LabResultStatus status) {
        this.status = status;
    }
    
    
}
