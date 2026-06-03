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
public class RevenueStatsResponse {
    private int month;
    private BigDecimal amount;

    public RevenueStatsResponse() {
    }

    public RevenueStatsResponse(int month, BigDecimal amount) {
        this.month = month;
        this.amount = amount;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    
    
}
