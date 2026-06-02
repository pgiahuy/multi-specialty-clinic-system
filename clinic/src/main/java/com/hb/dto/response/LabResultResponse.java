/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hb.enums.LabResultStatus;
import java.time.LocalDateTime;
import java.util.List;


/**
 *
 * @author DELL
 */
public class LabResultResponse {
    private Long id;
    private String patientName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime testAt;
    private LabResultStatus status;
    private String doctorName;
    private String doctorTestName;
    private List<LabResultDetailResponse> resultDetails;

    public LabResultResponse() {
    }

    public LabResultResponse(Long id, String patientName, LocalDateTime createdAt, LocalDateTime testAt, LabResultStatus status, String doctorName, String doctorTestName, List<LabResultDetailResponse> resultDetails) {
        this.id = id;
        this.patientName = patientName;
        this.createdAt = createdAt;
        this.testAt = testAt;
        this.status = status;
        this.doctorName = doctorName;
        this.doctorTestName = doctorTestName;
        this.resultDetails = resultDetails;
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
     * @return the createAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createAt the createAt to set
     */
    public void setCreatedAt(LocalDateTime createAt) {
        this.createdAt = createAt;
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

    /**
     * @return the resultDetails
     */
    public List<LabResultDetailResponse> getResultDetails() {
        return resultDetails;
    }

    /**
     * @param resultDetails the resultDetails to set
     */
    public void setResultDetails(List<LabResultDetailResponse> resultDetails) {
        this.resultDetails = resultDetails;
    }

    /**
     * @return the doctorName
     */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     * @param doctorName the doctorName to set
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    /**
     * @return the doctorTestName
     */
    public String getDoctorTestName() {
        return doctorTestName;
    }

    /**
     * @param doctorTestName the doctorTestName to set
     */
    public void setDoctorTestName(String doctorTestName) {
        this.doctorTestName = doctorTestName;
    }
    
    
    
}
