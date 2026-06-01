/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;

/**
 *
 * @author DELL
 */
public class TestDetailRequest {
    private Long id;
    private Long testId;
    private Long labResultId;
    private String value;
    private Boolean isAbnormal;

    public TestDetailRequest() {
    }

    public TestDetailRequest(Long id, Long testId, Long labResultId, String value, Boolean isAbnormal) {
        this.id = id;
        this.testId = testId;
        this.labResultId = labResultId;
        this.value = value;
        this.isAbnormal = isAbnormal;
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
     * @return the labResultId
     */
    public Long getLabResultId() {
        return labResultId;
    }

    /**
     * @param labResultId the labResultId to set
     */
    public void setLabResultId(Long labResultId) {
        this.labResultId = labResultId;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the isAbnormal
     */
    public Boolean getIsAbnormal() {
        return isAbnormal;
    }

    /**
     * @param isAbnormal the isAbnormal to set
     */
    public void setIsAbnormal(Boolean isAbnormal) {
        this.isAbnormal = isAbnormal;
    }
    
    
    
}
