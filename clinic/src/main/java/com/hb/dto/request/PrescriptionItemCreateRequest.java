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

    public PrescriptionItemCreateRequest(Long medicineId, int quantity) {
        this.medicineId = medicineId;
        this.quantity = quantity;
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
    
    
}
