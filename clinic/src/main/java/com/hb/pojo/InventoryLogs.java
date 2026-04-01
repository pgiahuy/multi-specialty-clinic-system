/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "inventory_logs")
@NamedQueries({
    @NamedQuery(name = "InventoryLogs.findAll", query = "SELECT i FROM InventoryLogs i"),
    @NamedQuery(name = "InventoryLogs.findById", query = "SELECT i FROM InventoryLogs i WHERE i.id = :id"),
    @NamedQuery(name = "InventoryLogs.findByChangeAmount", query = "SELECT i FROM InventoryLogs i WHERE i.changeAmount = :changeAmount"),
    @NamedQuery(name = "InventoryLogs.findByReason", query = "SELECT i FROM InventoryLogs i WHERE i.reason = :reason"),
    @NamedQuery(name = "InventoryLogs.findByCreatedAt", query = "SELECT i FROM InventoryLogs i WHERE i.createdAt = :createdAt")})
public class InventoryLogs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "change_amount")
    private Integer changeAmount;
    @Column(name = "reason")
    private String reason;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Medicines medicineId;

    public InventoryLogs() {
    }

    public InventoryLogs(Long id) {
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

    public Medicines getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Medicines medicineId) {
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
        if (!(object instanceof InventoryLogs)) {
            return false;
        }
        InventoryLogs other = (InventoryLogs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.InventoryLogs[ id=" + id + " ]";
    }
    
}
