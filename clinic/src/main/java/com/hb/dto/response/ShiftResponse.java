/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

/**
 *
 * @author HUY
 */
public class ShiftResponse {
    private Long id;
    private String sessionCode;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm:ss")
    private LocalTime endTime;
    
    private Integer maxPatients;
    private Integer minPatients;
    private String session;
    
    public ShiftResponse() {
    }

    public ShiftResponse(Long id, String sessionCode, LocalTime startTime, LocalTime endTime, Integer maxPatients, Integer minPatients, String session) {
        this.id = id;
        this.sessionCode = sessionCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxPatients = maxPatients;
        this.minPatients = minPatients;
        this.session = session;
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
     * @return the sessionCode
     */
    public String getSessionCode() {
        return sessionCode;
    }

    /**
     * @param sessionCode the sessionCode to set
     */
    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    /**
     * @return the startTime
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the maxPatients
     */
    public Integer getMaxPatients() {
        return maxPatients;
    }

    /**
     * @param maxPatients the maxPatients to set
     */
    public void setMaxPatients(Integer maxPatients) {
        this.maxPatients = maxPatients;
    }

    /**
     * @return the minPatients
     */
    public Integer getMinPatients() {
        return minPatients;
    }

    /**
     * @param minPatients the minPatients to set
     */
    public void setMinPatients(Integer minPatients) {
        this.minPatients = minPatients;
    }

    /**
     * @return the session
     */
    public String getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(String session) {
        this.session = session;
    }
    
    
}
