/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;


import com.hb.enums.AppointmentStatus;
import java.util.Date;

/**
 *
 * @author HUY
 */


public class AppointmentCreateRequest {

    private Long patientId;
    private Long scheduleId;
    private Long appId;
    private AppointmentStatus status;

    public AppointmentCreateRequest() {
    }

    public AppointmentCreateRequest(Long patientId, Long scheduleId) {
        this.patientId = patientId;
        this.scheduleId = scheduleId;
    }

    /**
     * @return the patientId
     */
    public Long getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the scheduleId
     */
    public Long getScheduleId() {
        return scheduleId;
    }

    /**
     * @param scheduleId the scheduleId to set
     */
    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    /**
     * @return the appId
     */
    public Long getAppId() {
        return appId;
    }

    /**
     * @return the status
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * @param appId the appId to set
     */
    public void setAppId(Long appId) {
        this.appId = appId;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
    
}
