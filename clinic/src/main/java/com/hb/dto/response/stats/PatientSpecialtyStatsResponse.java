/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response.stats;

/**
 *
 * @author HUY
 */
public class PatientSpecialtyStatsResponse {
    private String specialtyName;
    private Long count;

    public PatientSpecialtyStatsResponse() {
    }

    public PatientSpecialtyStatsResponse(String specialtyName, Long count) {
        this.specialtyName = specialtyName;
        this.count = count;
    }

    /**
     * @return the specialtyName
     */
    public String getSpecialtyName() {
        return specialtyName;
    }

    /**
     * @param specialtyName the specialtyName to set
     */
    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    /**
     * @return the count
     */
    public Long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Long count) {
        this.count = count;
    }
    
    
}
