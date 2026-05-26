/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request.form;

import com.hb.enums.SessionShift;
import java.time.LocalTime;

/**
 *
 * @author HUY
 */
public class ShiftForm {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private SessionShift session;
    private Integer minPatients;
    private Integer maxPatients;
    public ShiftForm() {
    }

    public ShiftForm(Long id, LocalTime startTime, LocalTime endTime, SessionShift session, Integer minPatients, Integer maxPatients) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.session = session;
        this.minPatients = minPatients;
        this.maxPatients = maxPatients;
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
     * @return the session
     */
    public SessionShift getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(SessionShift session) {
        this.session = session;
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

    
}
