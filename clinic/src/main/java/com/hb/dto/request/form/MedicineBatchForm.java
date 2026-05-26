/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hb.pojo.Medicine;
import java.time.LocalDate;

/**
 *
 * @author HUY
 */
public class MedicineBatchForm {
    private Long id;
    private String batchCode;
    private LocalDate importDate;
    private LocalDate expiryDate;
    private int quantity;
    private Medicine medicine;

    public MedicineBatchForm() {
    }

    public MedicineBatchForm(Long id, String batchCode, LocalDate importDate, LocalDate expiryDate, int quantity, Medicine medicine) {
        this.id = id;
        this.batchCode = batchCode;
        this.importDate = importDate;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.medicine = medicine;
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
     * @return the batchCode
     */
    public String getBatchCode() {
        return batchCode;
    }

    /**
     * @param batchCode the batchCode to set
     */
    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    /**
     * @return the importDate
     */
    public LocalDate getImportDate() {
        return importDate;
    }

    /**
     * @param importDate the importDate to set
     */
    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    /**
     * @return the expiryDate
     */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
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
     * @return the medicine
     */
    public Medicine getMedicine() {
        return medicine;
    }

    /**
     * @param medicine the medicine to set
     */
    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
    

    
}
    
