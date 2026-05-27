/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hb.enums.PaymentItemType;
import com.hb.enums.PaymentMethod;
import com.hb.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author DELL
 */
public class PaymentItemResponse {
    private Long id;
    private PaymentItemType type;
    private PaymentStatus status;
    private String testName;
    
    private BigDecimal amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDateTime paidAt;
    private String stransId;
    private PaymentMethod method;

    public PaymentItemResponse() {
    }

    public PaymentItemResponse(Long id, PaymentItemType type, PaymentStatus status, String testName, BigDecimal amount, LocalDateTime createdAt, LocalDateTime paidAt, String stransId, PaymentMethod method) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.testName = testName;
        this.amount = amount;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.stransId = stransId;
        this.method = method;
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
     * @return the type
     */
    public PaymentItemType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(PaymentItemType type) {
        this.type = type;
    }

    /**
     * @return the status
     */
    public PaymentStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    /**
     * @return the testName
     */
    public String getTestName() {
        return testName;
    }

    /**
     * @param testName the testName to set
     */
    public void setTestName(String testName) {
        this.testName = testName;
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

    /**
     * @return the createdAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the paidAt
     */
    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    /**
     * @param paidAt the paidAt to set
     */
    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    /**
     * @return the stransId
     */
    public String getStransId() {
        return stransId;
    }

    /**
     * @param stransId the stransId to set
     */
    public void setStransId(String stransId) {
        this.stransId = stransId;
    }

    /**
     * @return the method
     */
    public PaymentMethod getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    
    
    
    
}
