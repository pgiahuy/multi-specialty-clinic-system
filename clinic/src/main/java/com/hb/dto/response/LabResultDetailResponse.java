/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

/**
 *
 * @author DELL
 */
public class LabResultDetailResponse {
    private Long id;
    private String testName;
    private String value;
    private Boolean isAbnormal;
    private String normalRange;
    private String unit;

    public LabResultDetailResponse() {
    }

    public LabResultDetailResponse(Long id, String testName, String value, Boolean isAbnormal, String normalRange, String unit) {
        this.id = id;
        this.testName = testName;
        this.value = value;
        this.isAbnormal = isAbnormal;
        this.normalRange = normalRange;
        this.unit = unit;
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
     * @return the testName
     */
    public String getTestName() {
        return testName;
    }

    /**
     * @param testName the testName to set
     */
    public void setTestName(String testName) {
        this.testName = testName;
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

    /**
     * @return the normalRange
     */
    public String getNormalRange() {
        return normalRange;
    }

    /**
     * @param normalRange the normalRange to set
     */
    public void setNormalRange(String normalRange) {
        this.normalRange = normalRange;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    
}
