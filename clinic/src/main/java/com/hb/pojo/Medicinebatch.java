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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "medicinebatch")
@NamedQueries({
    @NamedQuery(name = "Medicinebatch.findAll", query = "SELECT m FROM Medicinebatch m"),
    @NamedQuery(name = "Medicinebatch.findById", query = "SELECT m FROM Medicinebatch m WHERE m.id = :id"),
    @NamedQuery(name = "Medicinebatch.findByBatchCode", query = "SELECT m FROM Medicinebatch m WHERE m.batchCode = :batchCode"),
    @NamedQuery(name = "Medicinebatch.findByExpiryDate", query = "SELECT m FROM Medicinebatch m WHERE m.expiryDate = :expiryDate"),
    @NamedQuery(name = "Medicinebatch.findByQuantity", query = "SELECT m FROM Medicinebatch m WHERE m.quantity = :quantity"),
    @NamedQuery(name = "Medicinebatch.findByImportDate", query = "SELECT m FROM Medicinebatch m WHERE m.importDate = :importDate")})
public class Medicinebatch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "batch_code")
    private String batchCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "expiry_date")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "import_date")
    @Temporal(TemporalType.DATE)
    private Date importDate;
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    @ManyToOne
    private Medicine medicineId;
    @OneToMany(mappedBy = "batchId")
    private Collection<InventoryLog> inventorylogCollection;

    public Medicinebatch() {
    }

    public Medicinebatch(Long id) {
        this.id = id;
    }

    public Medicinebatch(Long id, String batchCode, Date expiryDate, int quantity) {
        this.id = id;
        this.batchCode = batchCode;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Medicine getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Medicine medicineId) {
        this.medicineId = medicineId;
    }

    public Collection<InventoryLog> getInventorylogCollection() {
        return inventorylogCollection;
    }

    public void setInventorylogCollection(Collection<InventoryLog> inventorylogCollection) {
        this.inventorylogCollection = inventorylogCollection;
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
        if (!(object instanceof Medicinebatch)) {
            return false;
        }
        Medicinebatch other = (Medicinebatch) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.Medicinebatch[ id=" + id + " ]";
    }
    
}
