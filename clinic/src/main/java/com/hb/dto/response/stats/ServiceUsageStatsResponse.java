/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response.stats;

/**
 *
 * @author HUY
 */
public class ServiceUsageStatsResponse {
    private String serviceName;
    private Long usageCount;

    public ServiceUsageStatsResponse() {
    }

    public ServiceUsageStatsResponse(String serviceName, Long usageCount) {
        this.serviceName = serviceName;
        this.usageCount = usageCount;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return the usageCount
     */
    public Long getUsageCount() {
        return usageCount;
    }

    /**
     * @param usageCount the usageCount to set
     */
    public void setUsageCount(Long usageCount) {
        this.usageCount = usageCount;
    }
    
}
