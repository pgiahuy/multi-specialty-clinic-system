/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author DELL
 */
public class LabTestResultResponse {
    private Long id;
    private String testName;
    private String patientName;
    private String result;
    private String unit;
    private Boolean isNormal;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDateTime createAt;

    public LabTestResultResponse() {
    }

    public LabTestResultResponse(Long id,String testName, String patientName, String result, Boolean isNormal,LocalDateTime createAt, String unit) {
        this.id = id;
        this.testName = testName;
        this.patientName = patientName;
        this.result = result;
        this.isNormal = isNormal;
        this.createAt=createAt;
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
     * @return the patientName
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * @param patientName the patientName to set
     */
    public void setPatientName(String patientName) {
        this.patientName = patientName;
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
     * @return the isNormal
     */
    public Boolean isIsNormal() {
        return isNormal;
    }

    /**
     * @param isNormal the isNormal to set
     */
    public void setIsNormal(Boolean isNormal) {
        this.isNormal = isNormal;
    }

    /**
     * @return the createAt
     */
    public LocalDateTime getCreateAt() {
        return createAt;
    }

    /**
     * @param createAt the createAt to set
     */
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
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
