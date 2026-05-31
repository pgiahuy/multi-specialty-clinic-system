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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "medicine")
@NamedQueries({
    @NamedQuery(name = "Medicine.findAll", query = "SELECT m FROM Medicine m"),
    @NamedQuery(name = "Medicine.findById", query = "SELECT m FROM Medicine m WHERE m.id = :id"),
    @NamedQuery(name = "Medicine.findByCode", query = "SELECT m FROM Medicine m WHERE m.code = :code"),
    @NamedQuery(name = "Medicine.findByName", query = "SELECT m FROM Medicine m WHERE m.name = :name"),
    @NamedQuery(name = "Medicine.findByPrice", query = "SELECT m FROM Medicine m WHERE m.price = :price"),
    @NamedQuery(name = "Medicine.findBySecureUrl", query = "SELECT m FROM Medicine m WHERE m.secureUrl = :secureUrl"),
    @NamedQuery(name = "Medicine.findByPublicId", query = "SELECT m FROM Medicine m WHERE m.publicId = :publicId"),
    @NamedQuery(name = "Medicine.findByMinStockAlert", query = "SELECT m FROM Medicine m WHERE m.minStockAlert = :minStockAlert"),
    @NamedQuery(name = "Medicine.findByUnit", query = "SELECT m FROM Medicine m WHERE m.unit = :unit")})
public class Medicine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 50)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @Size(max = 500)
    @Column(name = "secure_url")
    private String secureUrl;
    @Size(max = 255)
    @Column(name = "public_id")
    private String publicId;
    @Column(name = "min_stock_alert")
    private Integer minStockAlert;
    @Size(max = 50)
    @Column(name = "unit")
    private String unit;
    @OneToMany(mappedBy = "medicineId")
    private Collection<MedicineBatch> medicineBatchCollection;
    @OneToMany(mappedBy = "medicineId")
    private Collection<InventoryLog> inventorylogCollection;
    @OneToMany(mappedBy = "medicineId")
    private Collection<PrescriptionItem> prescriptionItemCollection;

    public Medicine() {
    }

    public Medicine(Long id) {
        this.id = id;
    }

    public Medicine(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSecureUrl() {
        return secureUrl;
    }

    public void setSecureUrl(String secureUrl) {
        this.secureUrl = secureUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public Integer getMinStockAlert() {
        return minStockAlert;
    }

    public void setMinStockAlert(Integer minStockAlert) {
        this.minStockAlert = minStockAlert;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Collection<MedicineBatch> getMedicineBatchCollection() {
        return medicineBatchCollection;
    }

    public void setMedicineBatchCollection(Collection<MedicineBatch> medicineBatchCollection) {
        this.medicineBatchCollection = medicineBatchCollection;
    }

    public Collection<InventoryLog> getInventorylogCollection() {
        return inventorylogCollection;
    }

    public void setInventorylogCollection(Collection<InventoryLog> inventorylogCollection) {
        this.inventorylogCollection = inventorylogCollection;
    }

    public Collection<PrescriptionItem> getPrescriptionItemCollection() {
        return prescriptionItemCollection;
    }

    public void setPrescriptionItemCollection(Collection<PrescriptionItem> prescriptionItemCollection) {
        this.prescriptionItemCollection = prescriptionItemCollection;
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
        if (!(object instanceof Medicine)) {
            return false;
        }
        Medicine other = (Medicine) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.Medicine[ id=" + id + " ]";
    }
    
}
