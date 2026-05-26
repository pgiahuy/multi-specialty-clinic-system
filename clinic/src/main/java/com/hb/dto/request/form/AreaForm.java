/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request.form;

/**
 *
 * @author HUY
 */
public class AreaForm {
    private Long id;
    private String areaName;
    private Integer locationFloor;

    public AreaForm() {
    }

    public AreaForm(Long id, String areaName, Integer locationFloor) {
        this.id = id;
        this.areaName = areaName;
        this.locationFloor = locationFloor;
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
     * @return the areaName
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * @param areaName the areaName to set
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    /**
     * @return the locationFloor
     */
    public Integer getLocationFloor() {
        return locationFloor;
    }

    /**
     * @param locationFloor the locationFloor to set
     */
    public void setLocationFloor(Integer locationFloor) {
        this.locationFloor = locationFloor;
    }
    

    
}
