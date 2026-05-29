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
    private PaymentItemType type;
    
    private BigDecimal amount;
   

    public PaymentItemResponse() {
    }

    public PaymentItemResponse(Long id, Long paymentId, PaymentItemType type, BigDecimal amount) {
        this.id = id;
        this.paymentId = paymentId;
        this.type = type;
        
        this.amount = amount;
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


    
    
    
    
}
