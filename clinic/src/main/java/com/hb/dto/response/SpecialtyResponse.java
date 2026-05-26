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
public class SpecialtyResponse {
    private Long id;
    private String name;
    private BigDecimal fee;
    private String nameHod;

    public SpecialtyResponse() {
    }

    public SpecialtyResponse(Long id, String name, BigDecimal fee, String nameHod) {
        this.id = id;
        this.name = name;
        this.fee = fee;
        this.nameHod = nameHod;
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
     * @return the fee
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * @param fee the fee to set
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * @return the nameHod
     */
    public String getNameHod() {
        return nameHod;
    }

    /**
     * @param nameHod the nameHod to set
     */
    public void setNameHod(String nameHod) {
        this.nameHod = nameHod;
    }

    
   
}
