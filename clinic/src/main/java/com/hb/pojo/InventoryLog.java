/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.pojo;

import com.hb.enums.InventoryLogType;
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
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "inventory_log")
@NamedQueries({
    @NamedQuery(name = "InventoryLog.findAll", query = "SELECT i FROM InventoryLog i"),
    @NamedQuery(name = "InventoryLog.findById", query = "SELECT i FROM InventoryLog i WHERE i.id = :id"),
    @NamedQuery(name = "InventoryLog.findByChangeAmount", query = "SELECT i FROM InventoryLog i WHERE i.changeAmount = :changeAmount"),
    @NamedQuery(name = "InventoryLog.findByReason", query = "SELECT i FROM InventoryLog i WHERE i.reason = :reason"),
    @NamedQuery(name = "InventoryLog.findByReferenceId", query = "SELECT i FROM InventoryLog i WHERE i.referenceId = :referenceId"),
    @NamedQuery(name = "InventoryLog.findByCreatedAt", query = "SELECT i FROM InventoryLog i WHERE i.createdAt = :createdAt"),
    @NamedQuery(name = "InventoryLog.findByCreatedBy", query = "SELECT i FROM InventoryLog i WHERE i.createdBy = :createdBy")})
public class InventoryLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "change_amount")
    private int changeAmount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "reason")
    @Enumerated(EnumType.STRING)
    private InventoryLogType reason;
    @Column(name = "reference_id")
    private Long referenceId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Size(max = 255)
    @Column(name = "created_by")
    private String createdBy;
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    @ManyToOne
    private Medicine medicineId;
    @JoinColumn(name = "batch_id", referencedColumnName = "id")
    @ManyToOne
    private MedicineBatch batchId;

    public InventoryLog() {
    }

    public InventoryLog(Long id) {
        this.id = id;
    }

    public InventoryLog(Long id, int changeAmount, InventoryLogType reason) {
        this.id = id;
        this.changeAmount = changeAmount;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(int changeAmount) {
        this.changeAmount = changeAmount;
    }

    public InventoryLogType getReason() {
        return reason;
    }

    public void setReason(InventoryLogType reason) {
        this.reason = reason;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Medicine getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Medicine medicineId) {
        this.medicineId = medicineId;
    }

    public MedicineBatch getBatchId() {
        return batchId;
    }

    public void setBatchId(MedicineBatch batchId) {
        this.batchId = batchId;
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
        if (!(object instanceof InventoryLog)) {
            return false;
        }
        InventoryLog other = (InventoryLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.InventoryLog[ id=" + id + " ]";
    }
    
}
