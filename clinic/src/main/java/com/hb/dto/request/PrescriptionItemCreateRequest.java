/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;

/**
 *
 * @author HUY
 */
public class PrescriptionItemCreateRequest {
    private Long medicineId;
    private int quantity;
    private int daysToUse;
    private String note;

    public PrescriptionItemCreateRequest() {
    }

    public PrescriptionItemCreateRequest(Long medicineId, int quantity, int daysToUse, String note) {
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.daysToUse = daysToUse;
        this.note = note;
    }

    
    
    /**
     * @return the medicineId
     */
    public Long getMedicineId() {
        return medicineId;
    }

    /**
     * @param medicineId the medicineId to set
     */
    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the daysToUse
     */
    public int getDaysToUse() {
        return daysToUse;
    }

    /**
     * @param daysToUse the daysToUse to set
     */
    public void setDaysToUse(int daysToUse) {
        this.daysToUse = daysToUse;
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
    
    
}
