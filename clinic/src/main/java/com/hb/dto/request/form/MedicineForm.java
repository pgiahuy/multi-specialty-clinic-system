/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request.form;

import java.math.BigDecimal;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author HUY
 */
public class MedicineForm {
    private Long id;
    private String code;
    private String name;
    private BigDecimal price;
    private String unit;
    private MultipartFile image;
    private Integer minStockAlert;

    public MedicineForm() {
    }

    public MedicineForm(Long id, String code, String name, BigDecimal price, String unit, MultipartFile image, Integer minStockAlert) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.image = image;
        this.minStockAlert = minStockAlert;
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
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
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
     * @return the image
     */
    public MultipartFile getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(MultipartFile image) {
        this.image = image;
    }

    /**
     * @return the minStockAlert
     */
    public Integer getMinStockAlert() {
        return minStockAlert;
    }

    /**
     * @param minStockAlert the minStockAlert to set
     */
    public void setMinStockAlert(Integer minStockAlert) {
        this.minStockAlert = minStockAlert;
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
    
}
