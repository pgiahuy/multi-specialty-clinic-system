/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response.stats;

/**
 *
 * @author HUY
 */
public class TopDiseaseStatsResponse {
    private String diagnosis;
    private Long count;

    public TopDiseaseStatsResponse() {
    }

    public TopDiseaseStatsResponse(String diagnosis, Long count) {
        this.diagnosis = diagnosis;
        this.count = count;
    }

    /**
     * @return the diagnosis
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * @param diagnosis the diagnosis to set
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
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
