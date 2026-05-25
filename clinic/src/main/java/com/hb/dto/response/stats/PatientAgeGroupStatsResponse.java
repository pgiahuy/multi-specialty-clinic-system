/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response.stats;

/**
 *
 * @author HUY
 */
public class PatientAgeGroupStatsResponse {
    private String ageGroup;
    private Long count;

    public PatientAgeGroupStatsResponse() {
    }

    public PatientAgeGroupStatsResponse(String ageGroup, Long count) {
        this.ageGroup = ageGroup;
        this.count = count;
    }

    /**
     * @return the ageGroup
     */
    public String getAgeGroup() {
        return ageGroup;
    }

    /**
     * @param ageGroup the ageGroup to set
     */
    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
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
