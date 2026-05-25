/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;

/**
 *
 * @author DELL
 */
public class LabTestResultRequest {
    private Long id;
    private Long appointId;
    private Long testId;
    private String result;
    private Boolean isNormal;

    public LabTestResultRequest() {
    }

    public LabTestResultRequest(Long id, Long appointId, Long testId, String result, Boolean isNormal) {
        this.id = id;
        this.appointId = appointId;
        this.testId = testId;
        this.result = result;
        this.isNormal = isNormal;
    }

    public Boolean getIsNormal() {
        return isNormal;
    }

    public void setIsNormal(Boolean isNormal) {
        this.isNormal = isNormal;
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
     * @return the testId
     */
    public Long getTestId() {
        return testId;
    }

    /**
     * @param testId the testId to set
     */
    public void setTestId(Long testId) {
        this.testId = testId;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
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
    
    
}
