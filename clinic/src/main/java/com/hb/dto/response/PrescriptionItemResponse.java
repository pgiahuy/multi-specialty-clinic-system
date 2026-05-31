/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

import java.math.BigDecimal;

/**
 *
 * @author HUY
 */
public class PrescriptionItemResponse {
    private Long id;
    private String medicineName;
    private String medicineImage;
    private BigDecimal medicinePrice;
    private String note;
    private String unit;
    private Integer daysToUse;
    private Integer quantity;
    private Long medicineId;

    public PrescriptionItemResponse() {
    }

    public PrescriptionItemResponse(Long id, String medicineName, String medicineImage, BigDecimal medicinePrice, String note, String unit, Integer daysToUse, Integer quantity) {
        this.id = id;
        this.medicineName = medicineName;
        this.medicineImage = medicineImage;
        this.medicinePrice = medicinePrice;
        this.note = note;
        this.unit = unit;
        this.daysToUse = daysToUse;
        this.quantity = quantity;
    }

    public PrescriptionItemResponse(Long id, String medicineName, String medicineImage, String note, String unit, Integer daysToUse, Integer quantity) {
        this.id = id;
        this.medicineName = medicineName;
        this.medicineImage = medicineImage;
        this.note = note;
        this.unit = unit;
        this.daysToUse = daysToUse;
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
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the daysToUse
     */
    public Integer getDaysToUse() {
        return daysToUse;
    }

    /**
     * @param daysToUse the daysToUse to set
     */
    public void setDaysToUse(Integer daysToUse) {
        this.daysToUse = daysToUse;
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
     * @return the medicinePrice
     */
    public BigDecimal getMedicinePrice() {
        return medicinePrice;
    }

    /**
     * @param medicinePrice the medicinePrice to set
     */
    public void setMedicinePrice(BigDecimal medicinePrice) {
        this.medicinePrice = medicinePrice;
    }

    
    
    
}
