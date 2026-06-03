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
public class RevenueBySpecialtyStatsResponse {
    private String specialty;
    private BigDecimal amount;

    public RevenueBySpecialtyStatsResponse() {
    }

    public RevenueBySpecialtyStatsResponse(String specialty, BigDecimal amount) {
        this.specialty = specialty;
        this.amount = amount;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
