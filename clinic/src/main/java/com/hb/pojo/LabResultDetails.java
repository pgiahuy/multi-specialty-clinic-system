/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "lab_result_details")
@NamedQueries({
    @NamedQuery(name = "LabResultDetails.findAll", query = "SELECT l FROM LabResultDetails l"),
    @NamedQuery(name = "LabResultDetails.findById", query = "SELECT l FROM LabResultDetails l WHERE l.id = :id"),
    @NamedQuery(name = "LabResultDetails.findByValue", query = "SELECT l FROM LabResultDetails l WHERE l.value = :value"),
    @NamedQuery(name = "LabResultDetails.findByIsAbnormal", query = "SELECT l FROM LabResultDetails l WHERE l.isAbnormal = :isAbnormal")})
public class LabResultDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Size(max = 45)
    @Column(name = "value")
    private String value;
    @Column(name = "is_abnormal")
    private Short isAbnormal;
    @JoinColumn(name = "lab_results_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LabResults labResultsId;
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LabTests testId;

    public LabResultDetails() {
    }

    public LabResultDetails(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Short getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Short isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public LabResults getLabResultsId() {
        return labResultsId;
    }

    public void setLabResultsId(LabResults labResultsId) {
        this.labResultsId = labResultsId;
    }

    public LabTests getTestId() {
        return testId;
    }

    public void setTestId(LabTests testId) {
        this.testId = testId;
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
        if (!(object instanceof LabResultDetails)) {
            return false;
        }
        LabResultDetails other = (LabResultDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.LabResultDetails[ id=" + id + " ]";
    }
    
}
