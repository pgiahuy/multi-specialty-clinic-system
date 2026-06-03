/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response.stats;

import java.math.BigDecimal;

/**
 *
 * @author HUY
 */
public class RevenueByTypeStatsResponse {
    private String type;
    private BigDecimal amount;

    public RevenueByTypeStatsResponse() {
    }

    public RevenueByTypeStatsResponse(String type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
