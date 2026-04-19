/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;

import java.util.List;

/**
 *
 * @author HUY
 */
public class PrescriptionCreateRequest {
    private Long medicalRecordId; 
    private List<PrescriptionItemCreateRequest> items;

    public PrescriptionCreateRequest(Long medicalRecordId, List<PrescriptionItemCreateRequest> items) {
        this.medicalRecordId = medicalRecordId;
        this.items = items;
    }
    
    

    /**
     * @return the medicalRecordId
     */
    public Long getMedicalRecordId() {
        return medicalRecordId;
    }

    /**
     * @param medicalRecordId the medicalRecordId to set
     */
    public void setMedicalRecordId(Long medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    /**
     * @return the items
     */
    public List<PrescriptionItemCreateRequest> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<PrescriptionItemCreateRequest> items) {
        this.items = items;
    }
    
    
    
}
