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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "conversation")
@NamedQueries({
    @NamedQuery(name = "Conversation.findAll", query = "SELECT c FROM Conversation c"),
    @NamedQuery(name = "Conversation.findById", query = "SELECT c FROM Conversation c WHERE c.id = :id"),
    @NamedQuery(name = "Conversation.findByConversationType", query = "SELECT c FROM Conversation c WHERE c.conversationType = :conversationType"),
    @NamedQuery(name = "Conversation.findByIsActive", query = "SELECT c FROM Conversation c WHERE c.isActive = :isActive"),
    @NamedQuery(name = "Conversation.findByCreatedAt", query = "SELECT c FROM Conversation c WHERE c.createdAt = :createdAt")})
public class Conversation implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "conversation_type")
    private String conversationType;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "conversationId")
    private Collection<Appointment> appointmentCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "is_active")
    private Boolean isActive;
    
    
    
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Patient patientId;
    
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    @ManyToOne
    private User receiverId;

    public Conversation() {
    }

    public Conversation(Long id) {
        this.id = id;
    }

    public Conversation(Long id, String conversationType) {
        this.id = id;
        this.conversationType = conversationType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConversationType() {
        return conversationType;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public Patient getPatientId() {
        return patientId;
    }

    public void setPatientId(Patient patientId) {
        this.patientId = patientId;
    }

    public User getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(User receiverId) {
        this.receiverId = receiverId;
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
        if (!(object instanceof Conversation)) {
            return false;
        }
        Conversation other = (Conversation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.Conversation[ id=" + id + " ]";
    }

   

    public Collection<Appointment> getAppointmentCollection() {
        return appointmentCollection;
    }

    public void setAppointmentCollection(Collection<Appointment> appointmentCollection) {
        this.appointmentCollection = appointmentCollection;
    }

    
    
}
