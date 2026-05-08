/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

/**
 *
 * @author HUY
 */
public class PrescriptionItemResponse {
    private Long id;
    
    private String medicineName;
    private String medicineImage;
    private Integer quantity;

    public PrescriptionItemResponse() {
    }

    public PrescriptionItemResponse(Long id, String medicineName, String medicineImage, Integer quantity) {
        this.id = id;
        this.medicineName = medicineName;
        this.medicineImage = medicineImage;
        this.quantity = quantity;
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
     * @return the medicineName
     */
    public String getMedicineName() {
        return medicineName;
    }

    /**
     * @param medicineName the medicineName to set
     */
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    /**
     * @return the medicineImage
     */
    public String getMedicineImage() {
        return medicineImage;
    }

    /**
     * @param medicineImage the medicineImage to set
     */
    public void setMedicineImage(String medicineImage) {
        this.medicineImage = medicineImage;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    
}
