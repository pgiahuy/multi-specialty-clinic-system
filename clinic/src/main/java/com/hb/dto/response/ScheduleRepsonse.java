/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;


import java.util.Date;

/**
 *
 * @author HUY
 */

public class ScheduleRepsonse {
    private Long id;
    private Date date;
    private Integer maxPatients;
    private Integer currentPatients;
    private String doctorName;
//    private String doctorSpecialty;
    private String room;
    private String area;
    private String shiftStartTime;
    private String shiftEndTime;

    public ScheduleRepsonse() {
    }
    
    

    public ScheduleRepsonse(Long id, Date date, Integer maxPatients, Integer currentPatients, String doctorName , String room, String area, String shiftStartTime, String shiftEndTime) {
        this.id = id;
        this.date = date;
        this.maxPatients = maxPatients;
        this.currentPatients = currentPatients;
        this.doctorName = doctorName;
//        this.doctorSpecialty = doctorSpecialty;
        this.room = room;
        this.area = area;
        this.shiftStartTime = shiftStartTime;
        this.shiftEndTime = shiftEndTime;
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
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the maxPatients
     */
    public Integer getMaxPatients() {
        return maxPatients;
    }

    /**
     * @param maxPatients the maxPatients to set
     */
    public void setMaxPatients(Integer maxPatients) {
        this.maxPatients = maxPatients;
    }

    /**
     * @return the currentPatients
     */
    public Integer getCurrentPatients() {
        return currentPatients;
    }

    /**
     * @param currentPatients the currentPatients to set
     */
    public void setCurrentPatients(Integer currentPatients) {
        this.currentPatients = currentPatients;
    }

    /**
     * @return the doctorName
     */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     * @param doctorName the doctorName to set
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    /**
     * @return the doctorSpecialty
     */
//    public String getDoctorSpecialty() {
//        return doctorSpecialty;
//    }
//
//    /**
//     * @param doctorSpecialty the doctorSpecialty to set
//     */
//    public void setDoctorSpecialty(String doctorSpecialty) {
//        this.doctorSpecialty = doctorSpecialty;
//    }

    /**
     * @return the room
     */
    public String getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * @return the area
     */
    public String getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * @return the shiftStartTime
     */
    public String getShiftStartTime() {
        return shiftStartTime;
    }

    /**
     * @param shiftStartTime the shiftStartTime to set
     */
    public void setShiftStartTime(String shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    /**
     * @return the shiftEndTime
     */
    public String getShiftEndTime() {
        return shiftEndTime;
    }

    /**
     * @param shiftEndTime the shiftEndTime to set
     */
    public void setShiftEndTime(String shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }
    
    
    
}
