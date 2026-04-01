/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.pojo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "medicines")
@NamedQueries({
    @NamedQuery(name = "Medicines.findAll", query = "SELECT m FROM Medicines m"),
    @NamedQuery(name = "Medicines.findById", query = "SELECT m FROM Medicines m WHERE m.id = :id"),
    @NamedQuery(name = "Medicines.findByName", query = "SELECT m FROM Medicines m WHERE m.name = :name"),
    @NamedQuery(name = "Medicines.findByStock", query = "SELECT m FROM Medicines m WHERE m.stock = :stock"),
    @NamedQuery(name = "Medicines.findByExpirationDate", query = "SELECT m FROM Medicines m WHERE m.expirationDate = :expirationDate")})
public class Medicines implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "stock")
    private Integer stock;
    @Column(name = "expiration_date")
    @Temporal(TemporalType.DATE)
    private Date expirationDate;
    @OneToMany(mappedBy = "medicineId", fetch = FetchType.LAZY)
    private Collection<InventoryLogs> inventoryLogsCollection;
    @OneToMany(mappedBy = "medicineId", fetch = FetchType.LAZY)
    private Collection<PrescriptionItems> prescriptionItemsCollection;

    public Medicines() {
    }

    public Medicines(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Collection<InventoryLogs> getInventoryLogsCollection() {
        return inventoryLogsCollection;
    }

    public void setInventoryLogsCollection(Collection<InventoryLogs> inventoryLogsCollection) {
        this.inventoryLogsCollection = inventoryLogsCollection;
    }

    public Collection<PrescriptionItems> getPrescriptionItemsCollection() {
        return prescriptionItemsCollection;
    }

    public void setPrescriptionItemsCollection(Collection<PrescriptionItems> prescriptionItemsCollection) {
        this.prescriptionItemsCollection = prescriptionItemsCollection;
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
        if (!(object instanceof Medicines)) {
            return false;
        }
        Medicines other = (Medicines) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.Medicines[ id=" + id + " ]";
    }
    
}
