/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.pojo;

import com.hb.enums.PaymentMethod;
import com.hb.enums.PaymentStatus;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * @author DELL
 */
@Entity
@Table(name = "payment_items")
@NamedQueries({
    @NamedQuery(name = "PaymentItems.findAll", query = "SELECT p FROM PaymentItems p"),
    @NamedQuery(name = "PaymentItems.findById", query = "SELECT p FROM PaymentItems p WHERE p.id = :id"),
    @NamedQuery(name = "PaymentItems.findByItemType", query = "SELECT p FROM PaymentItems p WHERE p.itemType = :itemType"),
    @NamedQuery(name = "PaymentItems.findByAmount", query = "SELECT p FROM PaymentItems p WHERE p.amount = :amount"),
    @NamedQuery(name = "PaymentItems.findByCreatedAt", query = "SELECT p FROM PaymentItems p WHERE p.createdAt = :createdAt"),
    @NamedQuery(name = "PaymentItems.findByStatus", query = "SELECT p FROM PaymentItems p WHERE p.status = :status"),
    @NamedQuery(name = "PaymentItems.findByMethod", query = "SELECT p FROM PaymentItems p WHERE p.method = :method"),
    @NamedQuery(name = "PaymentItems.findByPaidAt", query = "SELECT p FROM PaymentItems p WHERE p.paidAt = :paidAt"),
    @NamedQuery(name = "PaymentItems.findByTransId", query = "SELECT p FROM PaymentItems p WHERE p.transId = :transId")})
public class PaymentItems implements Serializable {

    private static final long serialVersionUID = 1L;
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
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Size(max = 7)
    @Column(name = "status")
    private PaymentStatus status;
    @Size(max = 5)
    @Column(name = "method")
    private String method;
    @Column(name = "paid_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidAt;
    @Size(max = 100)
    @Column(name = "trans_id")
    private String transId;
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
    @Size(max = 100)
    @Column(name = "trans_id", length = 100)
    private String transId;

    public PaymentItems() {
    }

    public PaymentItems(Long id) {
        this.id = id;
    }

    public PaymentItems(Long id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Date getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public Appointment getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Appointment appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LabTests getLabTestId() {
        return labTestId;
    }

    public void setLabTestId(LabTests labTestId) {
        this.labTestId = labTestId;
    }

    public Payment getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Payment paymentId) {
        this.paymentId = paymentId;
    }

    public Prescription getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Prescription prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentItems)) {
            return false;
        }
        PaymentItems other = (PaymentItems) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.PaymentItems[ id=" + id + " ]";
    }
    
}
