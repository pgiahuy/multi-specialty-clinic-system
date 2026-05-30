/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;

/**
 *
 * @author HUY
 */
public class MedicalRecordCreateRequest {
    private Long id;
    private Long appointmentId;
    private String diagnosis;
    private String note;

    public MedicalRecordCreateRequest() {
    }

    public MedicalRecordCreateRequest(Long id, Long appointmentId, String diagnosis, String note) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.note = note;
    }

   
    /**
     * @return the appointId
     */
    public Long getAppointmentId() {
        return appointmentId;
    }

    /**
     * @param appointId the appointId to set
     */
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }


    /**
     * @return the diagnosis
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * @param diagnosis the diagnosis to set
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
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

   
    
}
