/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "lab_result")
@NamedQueries({
    @NamedQuery(name = "LabResult.findAll", query = "SELECT l FROM LabResult l"),
    @NamedQuery(name = "LabResult.findByStatus", query = "SELECT l FROM LabResult l WHERE l.status = :status"),
    @NamedQuery(name = "LabResult.findByCreatedAt", query = "SELECT l FROM LabResult l WHERE l.createdAt = :createdAt"),
    @NamedQuery(name = "LabResult.findByTestAt", query = "SELECT l FROM LabResult l WHERE l.testAt = :testAt"),
    @NamedQuery(name = "LabResult.findById", query = "SELECT l FROM LabResult l WHERE l.id = :id")})
public class LabResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 10)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Column(name = "test_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime testAt;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Appointment appointmentId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "labResultsId")
    private Collection<LabResultDetail> labResultDetailCollection;

    public LabResult() {
    }

    public LabResult(Long id) {
        this.id = id;
    }

    public LabResult(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getTestAt() {
        return testAt;
    }

    public void setTestAt(LocalDateTime testAt) {
        this.testAt = testAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Appointment getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Appointment appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Collection<LabResultDetail> getLabResultDetailCollection() {
        return labResultDetailCollection;
    }

    public void setLabResultDetailCollection(Collection<LabResultDetail> labResultDetailCollection) {
        this.labResultDetailCollection = labResultDetailCollection;
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
        if (!(object instanceof LabResult)) {
            return false;
        }
        LabResult other = (LabResult) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.LabResult[ id=" + id + " ]";
    }
    
}
