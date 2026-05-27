/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.pojo;

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
import java.util.Date;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "inventorylog")
@NamedQueries({
    @NamedQuery(name = "Inventorylog.findAll", query = "SELECT i FROM Inventorylog i"),
    @NamedQuery(name = "Inventorylog.findById", query = "SELECT i FROM Inventorylog i WHERE i.id = :id"),
    @NamedQuery(name = "Inventorylog.findByChangeAmount", query = "SELECT i FROM Inventorylog i WHERE i.changeAmount = :changeAmount"),
    @NamedQuery(name = "Inventorylog.findByReason", query = "SELECT i FROM Inventorylog i WHERE i.reason = :reason"),
    @NamedQuery(name = "Inventorylog.findByReferenceId", query = "SELECT i FROM Inventorylog i WHERE i.referenceId = :referenceId"),
    @NamedQuery(name = "Inventorylog.findByCreatedAt", query = "SELECT i FROM Inventorylog i WHERE i.createdAt = :createdAt"),
    @NamedQuery(name = "Inventorylog.findByCreatedBy", query = "SELECT i FROM Inventorylog i WHERE i.createdBy = :createdBy")})
public class Inventorylog implements Serializable {

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
    private String reason;
    @Column(name = "reference_id")
    private Integer referenceId;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "created_by")
    private Integer createdBy;
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    @ManyToOne
    private Medicine medicineId;
    @JoinColumn(name = "batch_id", referencedColumnName = "id")
    @ManyToOne
    private MedicineBatch batchId;

    public Inventorylog() {
    }

    public Inventorylog(Long id) {
        this.id = id;
    }

    public Inventorylog(Long id, int changeAmount, String reason) {
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
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
        if (!(object instanceof Inventorylog)) {
            return false;
        }
        Inventorylog other = (Inventorylog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.Inventorylog[ id=" + id + " ]";
    }
    
}
