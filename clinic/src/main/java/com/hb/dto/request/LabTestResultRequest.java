/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author DELL
 */
public class LabTestResultRequest {
    private Long id;
    private Long appointId;
    private LocalDateTime createdAt;
    private LocalDateTime testAt;
    private List<TestDetailRequest> details;
    

    public LabTestResultRequest() {
    }

    public LabTestResultRequest(Long id, Long appointId, LocalDateTime createdAt, LocalDateTime testAt, List<TestDetailRequest> details) {
        this.id = id;
        this.appointId = appointId;
        this.createdAt = createdAt;
        this.testAt = testAt;
        this.details = details;
    }

    

    
    
    /**
     * @return the appointId
     */
    public Long getAppointId() {
        return appointId;
    }

    /**
     * @param appointId the appointId to set
     */
    public void setAppointId(Long appointId) {
        this.appointId = appointId;
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
    public List<TestDetailRequest> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(List<TestDetailRequest> details) {
        this.details = details;
    }
    
    
}
