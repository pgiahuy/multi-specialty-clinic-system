/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 *
 * @author HUY
 */


public class ScheduleCreateRequest {
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày khám không được để trống")
    @Future(message = "Ngày khám phải ở tương lai")
    private LocalDate date;
    @NotNull
    private Integer maxPatients;
    @NotNull
    private Long specialtyId;
    @NotNull
    private Long roomId;
    @NotNull
    private Long shiftId;
    private Long doctorId;

    public ScheduleCreateRequest() {
    }

    public ScheduleCreateRequest(LocalDate date, Integer maxPatients, Long SpecialtyId, Long roomId, Long shiftId, Long doctorId) {
        this.date = date;
        this.maxPatients = maxPatients;
        this.specialtyId = SpecialtyId;
        this.roomId = roomId;
        this.shiftId = shiftId;
        this.doctorId = doctorId;
    }

    /**
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
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
     * @return the SpecialtyId
     */
    public Long getSpecialtyId() {
        return specialtyId;
    }

    /**
     * @param SpecialtyId the SpecialtyId to set
     */
    public void setSpecialtyId(Long specialtyId) {
        this.specialtyId = specialtyId;
    }

    /**
     * @return the roomId
     */
    public Long getRoomId() {
        return roomId;
    }

    /**
     * @param roomId the roomId to set
     */
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    /**
     * @return the shiftId
     */
    public Long getShiftId() {
        return shiftId;
    }

    /**
     * @param shiftId the shiftId to set
     */
    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return the doctorId
     */
    public Long getDoctorId() {
        return doctorId;
    }

    /**
     * @param doctorId the doctorId to set
     */
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
    
    
    

    
}
