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
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "lab_results")
@NamedQueries({
    @NamedQuery(name = "LabResults.findAll", query = "SELECT l FROM LabResults l"),
    @NamedQuery(name = "LabResults.findById", query = "SELECT l FROM LabResults l WHERE l.id = :id"),
    @NamedQuery(name = "LabResults.findByResultValue", query = "SELECT l FROM LabResults l WHERE l.resultValue = :resultValue"),
    @NamedQuery(name = "LabResults.findByIsAbnormal", query = "SELECT l FROM LabResults l WHERE l.isAbnormal = :isAbnormal"),
    @NamedQuery(name = "LabResults.findByPdfUrl", query = "SELECT l FROM LabResults l WHERE l.pdfUrl = :pdfUrl"),
    @NamedQuery(name = "LabResults.findByCreatedAt", query = "SELECT l FROM LabResults l WHERE l.createdAt = :createdAt")})
public class LabResults implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 50)
    @Column(name = "result_value")
    private String resultValue;
    @Column(name = "is_abnormal")
    private Boolean isAbnormal;
    @Size(max = 255)
    @Column(name = "pdf_url")
    private String pdfUrl;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    @ManyToOne
    private Appointment appointmentId;
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    @ManyToOne
    private LabTests testId;
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    @ManyToOne
    private Patient patientId;

    public LabResults() {
    }

    public LabResults(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public Boolean getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Boolean isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Appointment getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Appointment appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LabTests getTestId() {
        return testId;
    }

    public void setTestId(LabTests testId) {
        this.testId = testId;
    }

    public Patient getPatientId() {
        return patientId;
    }

    public void setPatientId(Patient patientId) {
        this.patientId = patientId;
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
        if (!(object instanceof LabResults)) {
            return false;
        }
        LabResults other = (LabResults) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.LabResults[ id=" + id + " ]";
    }
    
}
