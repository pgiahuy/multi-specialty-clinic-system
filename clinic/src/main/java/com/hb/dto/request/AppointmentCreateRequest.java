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
    
    private Long id;
    private Long patientId;
    private Long scheduleId;

    public AppointmentCreateRequest() {
    }

    public AppointmentCreateRequest(Long id, Long patientId, Long scheduleId) {
        this.id = id;
        this.patientId = patientId;
        this.scheduleId = scheduleId;
        this.status = status;
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

    
    
}
