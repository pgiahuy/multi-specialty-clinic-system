/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.pojo;

import com.hb.enums.PaymentItemType;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

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
    @NamedQuery(name = "PaymentItems.findByReferenceId", query = "SELECT p FROM PaymentItems p WHERE p.referenceId = :referenceId")})
public class PaymentItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "item_type")
    @Enumerated(EnumType.STRING)
    private PaymentItemType itemType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    private BigDecimal amount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reference_id")
    private long referenceId;
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Payment paymentId;

    public PaymentItems() {
    }

    public PaymentItems(Long id) {
        this.id = id;
    }

    public PaymentItems(Long id, PaymentItemType itemType, BigDecimal amount, long referenceId) {
        this.id = id;
        this.itemType = itemType;
        this.amount = amount;
        this.referenceId = referenceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentItemType getItemType() {
        return itemType;
    }

    public void setItemType(PaymentItemType itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(long referenceId) {
        this.referenceId = referenceId;
    }

    public Payment getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Payment paymentId) {
        this.paymentId = paymentId;
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
