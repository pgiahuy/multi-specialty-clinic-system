/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author HUY
 */
public class AppointmentResponse {
    
    private Long id;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDateTime createdAt;
    private Long patientId;
    private String patientFullName;
    private String doctorFullName;
    private String specialtyName;
    private BigDecimal price;
    private String appointmentDate;
    private String session;
    private String timeSlot;
    private String roomName;
    private String areaName;

    public AppointmentResponse() {
    }

    public AppointmentResponse(Long id, String status, LocalDateTime createdAt, Long patientId, String patientFullName, String doctorFullName, String specialtyName, BigDecimal price, String appointmentDate, String session, String timeSlot, String roomName, String areaName) {
        this.id = id;
        this.status = status;
        this.createdAt = createdAt;
        this.patientId = patientId;
        this.patientFullName = patientFullName;
        this.doctorFullName = doctorFullName;
        this.specialtyName = specialtyName;
        this.price = price;
        this.appointmentDate = appointmentDate;
        this.session = session;
        this.timeSlot = timeSlot;
        this.roomName = roomName;
        this.areaName = areaName;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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
     * @return the patientFullName
     */
    public String getPatientFullName() {
        return patientFullName;
    }

    /**
     * @param patientFullName the patientFullName to set
     */
    public void setPatientFullName(String patientFullName) {
        this.patientFullName = patientFullName;
    }

    /**
     * @return the doctorFullName
     */
    public String getDoctorFullName() {
        return doctorFullName;
    }

    /**
     * @param doctorFullName the doctorFullName to set
     */
    public void setDoctorFullName(String doctorFullName) {
        this.doctorFullName = doctorFullName;
    }

    /**
     * @return the specialtyName
     */
    public String getSpecialtyName() {
        return specialtyName;
    }

    /**
     * @param specialtyName the specialtyName to set
     */
    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    /**
     * @return the appointmentDate
     */
    public String getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * @param appointmentDate the appointmentDate to set
     */
    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
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

    /**
     * @return the timeSlot
     */
    public String getTimeSlot() {
        return timeSlot;
    }

    /**
     * @param timeSlot the timeSlot to set
     */
    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    /**
     * @return the roomName
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * @param roomName the roomName to set
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * @return the areaName
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * @param areaName the areaName to set
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName;
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
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    

}
