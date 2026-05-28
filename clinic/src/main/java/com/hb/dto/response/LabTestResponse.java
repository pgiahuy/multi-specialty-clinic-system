/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

import java.math.BigDecimal;

/**
 *
 * @author DELL
 */
public class LabTestResponse {
    private Long id;
    private String testName;
    private String unit;
    private String normalRange;
    private BigDecimal price;

    public LabTestResponse() {
    }

    public LabTestResponse(Long id, String testName, String unit, String normalRange, BigDecimal price) {
        this.id = id;
        this.testName = testName;
        this.unit = unit;
        this.normalRange = normalRange;
        this.price = price;
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
     * @return the testName
     */
    public String getTestName() {
        return testName;
    }

    /**
     * @param testName the testName to set
     */
    public void setTestName(String testName) {
        this.testName = testName;
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
     * @return the normalRange
     */
    public String getNormalRange() {
        return normalRange;
    }

    /**
     * @param normalRange the normalRange to set
     */
    public void setNormalRange(String normalRange) {
        this.normalRange = normalRange;
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
