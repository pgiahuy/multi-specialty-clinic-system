/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.pojo;

import java.io.Serializable;
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

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "prescription_item")
@NamedQueries({
    @NamedQuery(name = "PrescriptionItem.findAll", query = "SELECT p FROM PrescriptionItem p"),
    @NamedQuery(name = "PrescriptionItem.findById", query = "SELECT p FROM PrescriptionItem p WHERE p.id = :id"),
    @NamedQuery(name = "PrescriptionItem.findByQuantity", query = "SELECT p FROM PrescriptionItem p WHERE p.quantity = :quantity")})
public class PrescriptionItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "quantity")
    private Integer quantity;
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicineId;
    @JoinColumn(name = "prescription_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Prescription prescriptionId;

    public PrescriptionItem() {
    }

    public PrescriptionItem(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Medicine getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Medicine medicineId) {
        this.medicineId = medicineId;
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
        if (!(object instanceof PrescriptionItem)) {
            return false;
        }
        PrescriptionItem other = (PrescriptionItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.PrescriptionItem[ id=" + id + " ]";
    }
    
}
