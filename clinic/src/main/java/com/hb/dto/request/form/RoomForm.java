/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request.form;

/**
 *
 * @author HUY
 */
public class RoomForm {
    private Long id;
    private String roomNumber;
    private Long areaId;

    public RoomForm() {
    }

    public RoomForm(Long id, String roomNumber, Long areaId) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.areaId = areaId;
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
     * @return the roomNumber
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * @param roomNumber the roomNumber to set
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * @return the areaId
     */
    public Long getAreaId() {
        return areaId;
    }

    /**
     * @param areaId the areaId to set
     */
    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    
}
