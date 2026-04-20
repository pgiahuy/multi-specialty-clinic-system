/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.pojo;

import com.hb.enums.PaymentStatus;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "payment_items")
@NamedQueries({
    @NamedQuery(name = "PaymentItems.findAll", query = "SELECT p FROM PaymentItems p"),
    @NamedQuery(name = "PaymentItems.findById", query = "SELECT p FROM PaymentItems p WHERE p.id = :id"),
    @NamedQuery(name = "PaymentItems.findByItemType", query = "SELECT p FROM PaymentItems p WHERE p.itemType = :itemType"),
    @NamedQuery(name = "PaymentItems.findByAmount", query = "SELECT p FROM PaymentItems p WHERE p.amount = :amount"),
    
    @NamedQuery(name = "PaymentItems.findByStatus", query = "SELECT p FROM PaymentItems p WHERE p.status = :status")})
public class PaymentItems implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 12)
    @Column(name = "item_type")
    private String itemType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    private BigDecimal amount;
    
    @Size(max = 7)
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;
    
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    @ManyToOne
    private Appointment appointmentId;
    @JoinColumn(name = "lab_test_id", referencedColumnName = "id")
    @ManyToOne
    private LabTests labTestId;
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Payment paymentId;
    @JoinColumn(name = "prescription_id", referencedColumnName = "id")
    @ManyToOne
    private Prescription prescriptionId;

    public PaymentItems() {
    }

    public PaymentItems(Long id) {
        this.id = id;
    }

    public PaymentItems(Long id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
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
     * @return the itemType
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
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
     * @return the appointmentId
     */
    public Appointment getAppointmentId() {
        return appointmentId;
    }

    /**
     * @param appointmentId the appointmentId to set
     */
    public void setAppointmentId(Appointment appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * @return the labTestId
     */
    public LabTests getLabTestId() {
        return labTestId;
    }

    /**
     * @param labTestId the labTestId to set
     */
    public void setLabTestId(LabTests labTestId) {
        this.labTestId = labTestId;
    }

    /**
     * @return the paymentId
     */
    public Payment getPaymentId() {
        return paymentId;
    }

    /**
     * @param paymentId the paymentId to set
     */
    public void setPaymentId(Payment paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * @return the prescriptionId
     */
    public Prescription getPrescriptionId() {
        return prescriptionId;
    }

    /**
     * @param prescriptionId the prescriptionId to set
     */
    public void setPrescriptionId(Prescription prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    
}
