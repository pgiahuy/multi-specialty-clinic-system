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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "prescription_item")
@NamedQueries({
    @NamedQuery(name = "PrescriptionItem.findAll", query = "SELECT p FROM PrescriptionItem p"),
    @NamedQuery(name = "PrescriptionItem.findById", query = "SELECT p FROM PrescriptionItem p WHERE p.id = :id"),
    @NamedQuery(name = "PrescriptionItem.findByQuantity", query = "SELECT p FROM PrescriptionItem p WHERE p.quantity = :quantity"),
    @NamedQuery(name = "PrescriptionItem.findByDaysToUse", query = "SELECT p FROM PrescriptionItem p WHERE p.daysToUse = :daysToUse"),
    @NamedQuery(name = "PrescriptionItem.findByNote", query = "SELECT p FROM PrescriptionItem p WHERE p.note = :note")})
public class PrescriptionItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "days_to_use")
    private Integer daysToUse;
    @Size(max = 255)
    @Column(name = "note")
    private String note;
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    @ManyToOne
    private Medicine medicineId;
    @JoinColumn(name = "prescription_id", referencedColumnName = "id")
    @ManyToOne
    private Prescription prescriptionId;

    public PrescriptionItem() {
    }

    public PrescriptionItem(Long id) {
        this.id = id;
    }

    public PrescriptionItem(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getDaysToUse() {
        return daysToUse;
    }

    public void setDaysToUse(Integer daysToUse) {
        this.daysToUse = daysToUse;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
