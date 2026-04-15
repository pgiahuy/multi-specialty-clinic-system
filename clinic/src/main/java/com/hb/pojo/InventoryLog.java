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
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "inventory_log")
@NamedQueries({
    @NamedQuery(name = "InventoryLog.findAll", query = "SELECT i FROM InventoryLog i"),
    @NamedQuery(name = "InventoryLog.findById", query = "SELECT i FROM InventoryLog i WHERE i.id = :id"),
    @NamedQuery(name = "InventoryLog.findByChangeAmount", query = "SELECT i FROM InventoryLog i WHERE i.changeAmount = :changeAmount"),
    @NamedQuery(name = "InventoryLog.findByReason", query = "SELECT i FROM InventoryLog i WHERE i.reason = :reason"),
    @NamedQuery(name = "InventoryLog.findByCreatedAt", query = "SELECT i FROM InventoryLog i WHERE i.createdAt = :createdAt")})
public class InventoryLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "change_amount")
    private Integer changeAmount;
    @Size(max = 255)
    @Column(name = "reason")
    private String reason;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    @ManyToOne
    private Medicine medicineId;

    public InventoryLog() {
    }

    public InventoryLog(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Integer changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Medicine getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Medicine medicineId) {
        this.medicineId = medicineId;
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
