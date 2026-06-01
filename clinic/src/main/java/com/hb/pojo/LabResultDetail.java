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
 * @author HUY
 */
@Entity
@Table(name = "lab_result_detail")
@NamedQueries({
    @NamedQuery(name = "LabResultDetail.findAll", query = "SELECT l FROM LabResultDetail l"),
    @NamedQuery(name = "LabResultDetail.findById", query = "SELECT l FROM LabResultDetail l WHERE l.id = :id"),
    @NamedQuery(name = "LabResultDetail.findByValue", query = "SELECT l FROM LabResultDetail l WHERE l.value = :value"),
    @NamedQuery(name = "LabResultDetail.findByIsAbnormal", query = "SELECT l FROM LabResultDetail l WHERE l.isAbnormal = :isAbnormal")})
public class LabResultDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 45)
    @Column(name = "value")
    private String value;
    @Column(name = "is_abnormal")
    private Boolean isAbnormal;
    @NotNull
    @JoinColumn(name = "lab_result_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LabResult labResultId;
    @NotNull
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LabTest testId;

    public LabResultDetail() {
    }

    public LabResultDetail(Long id) {
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

    public Boolean getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Boolean isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public LabResult getLabResultsId() {
        return labResultId;
    }

    public void setLabResultsId(LabResult labResultsId) {
        this.labResultId = labResultsId;
    }

    public LabTest getTestId() {
        return testId;
    }

    public void setTestId(LabTest testId) {
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
        if (!(object instanceof LabResultDetail)) {
            return false;
        }
        LabResultDetail other = (LabResultDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.LabResultDetail[ id=" + id + " ]";
    }
    
}
