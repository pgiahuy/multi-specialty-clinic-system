/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

import com.hb.enums.PaymentItemType;
import java.math.BigDecimal;

/**
 *
 * @author DELL
 */
public class PaymentItemResponse {
    private Long id;
    private Long paymentId;
    private PaymentItemType itemType;
    private BigDecimal amount;
    private Long referenceId;
    
    
   

    public PaymentItemResponse() {
    }

    public PaymentItemResponse(Long id, Long paymentId, PaymentItemType itemType, BigDecimal amount, Long referenceId) {
        this.id = id;
        this.paymentId = paymentId;
        this.itemType = itemType;
        this.amount = amount;
        this.referenceId = referenceId;
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
     * @return the paymentId
     */
    public Long getPaymentId() {
        return paymentId;
    }

    /**
     * @param paymentId the paymentId to set
     */
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * @return the itemType
     */
    public PaymentItemType getItemType() {
        return itemType;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setItemType(PaymentItemType itemType) {
        this.itemType = itemType;
    }

    /**
     * @return the referenceId
     */
    public Long getReferenceId() {
        return referenceId;
    }

    /**
     * @param referenceId the referenceId to set
     */
    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }


    
    
    
    
}
