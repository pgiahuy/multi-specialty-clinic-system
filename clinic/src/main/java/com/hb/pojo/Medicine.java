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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "medicine")
@NamedQueries({
    @NamedQuery(name = "Medicine.findAll", query = "SELECT m FROM Medicine m"),
    @NamedQuery(name = "Medicine.findById", query = "SELECT m FROM Medicine m WHERE m.id = :id"),
    @NamedQuery(name = "Medicine.findByName", query = "SELECT m FROM Medicine m WHERE m.name = :name"),
    @NamedQuery(name = "Medicine.findByStock", query = "SELECT m FROM Medicine m WHERE m.stock = :stock"),
    @NamedQuery(name = "Medicine.findByExpirationDate", query = "SELECT m FROM Medicine m WHERE m.expirationDate = :expirationDate"),
    @NamedQuery(name = "Medicine.findBySecureUrl", query = "SELECT m FROM Medicine m WHERE m.secureUrl = :secureUrl"),
    @NamedQuery(name = "Medicine.findByPublicId", query = "SELECT m FROM Medicine m WHERE m.publicId = :publicId"),
    @NamedQuery(name = "Medicine.findByCode", query = "SELECT m FROM Medicine m WHERE m.code = :code"),
    @NamedQuery(name = "Medicine.findByPrice", query = "SELECT m FROM Medicine m WHERE m.price = :price")})
public class Medicine implements Serializable {

    @Size(max = 100)
    @Column(name = "name")
    private String name;
    @Size(max = 255)
    @Column(name = "secure_url")
    private String secureUrl;
    @Size(max = 255)
    @Column(name = "public_id")
    private String publicId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "code")
    private String code;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "stock")
    private Integer stock;
    @Column(name = "expiration_date")
    @Temporal(TemporalType.DATE)
    private Date expirationDate;
    @OneToMany(mappedBy = "medicineId")
    private Collection<InventoryLog> inventoryLogCollection;
    @OneToMany(mappedBy = "medicineId")
    private Collection<PrescriptionItem> prescriptionItemCollection;

    public Medicine() {
    }

    public Medicine(Long id) {
        this.id = id;
    }

    public Medicine(Long id, String code, BigDecimal price) {
        this.id = id;
        this.code = code;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public Collection<InventoryLog> getInventoryLogCollection() {
        return inventoryLogCollection;
    }

    public void setInventoryLogCollection(Collection<InventoryLog> inventoryLogCollection) {
        this.inventoryLogCollection = inventoryLogCollection;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
}
