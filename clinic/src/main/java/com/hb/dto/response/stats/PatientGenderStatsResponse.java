package com.hb.dto.response.stats;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author HUY
 */
public class PatientGenderStatsResponse {
    private String gender;
    private Long count;

    public PatientGenderStatsResponse(String gender, Long count) {
        this.gender = gender;
        this.count = count;
    }

    public PatientGenderStatsResponse() {
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
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
