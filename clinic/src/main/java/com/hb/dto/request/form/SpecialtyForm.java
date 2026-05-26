/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request.form;

import com.hb.pojo.Doctor;
import java.math.BigDecimal;

/**
 *
 * @author HUY
 */
public class SpecialtyForm {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long hodId;

    public SpecialtyForm() {
    }

    public SpecialtyForm(Long id, String name, BigDecimal price, Long hodId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.hodId = hodId;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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

    /**
     * @return the hodId
     */
    public Long getHodId() {
        return hodId;
    }

    /**
     * @param hodId the hodId to set
     */
    public void setHodId(Long hodId) {
        this.hodId = hodId;
    }
    
    
}
