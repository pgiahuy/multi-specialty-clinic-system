/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hb.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author DELL
 */
public class PaymentResponse {
    private Long id;
    private String patientName;
    private BigDecimal totalAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
    private PaymentStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDateTime paidAt;
    private Long appointmentId;
    private List<PaymentItemResponse> paymentItems;
    

    public PaymentResponse() {
        
    }

    public PaymentResponse(Long id, String patientName, BigDecimal totalAmount, LocalDate createdAt, PaymentStatus status, LocalDateTime paidAt, Long appointmentId, List<PaymentItemResponse> paymentItems) {
        this.id = id;
        this.patientName = patientName;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.status = status;
        this.paidAt = paidAt;
        this.appointmentId = appointmentId;
        this.paymentItems = paymentItems;
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
     * @return the patientName
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * @param patientName the patientName to set
     */
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    

    /**
     * @return the totalAmount
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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
     * @return the appointmentId
     */
    public Long getAppointmentId() {
        return appointmentId;
    }

    /**
     * @param appointmentId the appointmentId to set
     */
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * @return the createdAt
     */
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the paymentItems
     */
    public List<PaymentItemResponse> getPaymentItems() {
        return paymentItems;
    }

    /**
     * @param paymentItems the paymentItems to set
     */
    public void setPaymentItems(List<PaymentItemResponse> paymentItems) {
        this.paymentItems = paymentItems;
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
    
}
