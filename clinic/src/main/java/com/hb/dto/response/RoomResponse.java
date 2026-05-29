package com.hb.dto.response;

public class RoomResponse {
    private Long id;
    private String roomNumber;
    private String areaName;
    private Integer locationFloor;

    public RoomResponse() {
    }

    public RoomResponse(Long id, String roomNumber, String areaName, Integer locationFloor) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.areaName = areaName;
        this.locationFloor = locationFloor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getLocationFloor() {
        return locationFloor;
    }

    public void setLocationFloor(Integer locationFloor) {
        this.locationFloor = locationFloor;
    }
}