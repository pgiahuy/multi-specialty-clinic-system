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
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "shifts")
@NamedQueries({
    @NamedQuery(name = "Shifts.findAll", query = "SELECT s FROM Shifts s"),
    @NamedQuery(name = "Shifts.findById", query = "SELECT s FROM Shifts s WHERE s.id = :id"),
    @NamedQuery(name = "Shifts.findByStartTime", query = "SELECT s FROM Shifts s WHERE s.startTime = :startTime"),
    @NamedQuery(name = "Shifts.findByEndTime", query = "SELECT s FROM Shifts s WHERE s.endTime = :endTime"),
    @NamedQuery(name = "Shifts.findBySession", query = "SELECT s FROM Shifts s WHERE s.session = :session"),
    @NamedQuery(name = "Shifts.findByMaxPatinets", query = "SELECT s FROM Shifts s WHERE s.maxPatinets = :maxPatinets"),
    @NamedQuery(name = "Shifts.findByMinPatients", query = "SELECT s FROM Shifts s WHERE s.minPatients = :minPatients")})
public class Shifts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIME)
    private LocalTime startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIME)
    private LocalTime endTime;
    @Size(max = 9)
    @Column(name = "session")
    private String session;
    @Column(name = "max_patinets")
    private Integer maxPatinets;
    @Column(name = "min_patients")
    private Integer minPatients;
    @OneToMany(mappedBy = "shiftId")
    private Collection<Schedules> schedulesCollection;

    public Shifts() {
    }

    public Shifts(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Integer getMaxPatinets() {
        return maxPatinets;
    }

    public void setMaxPatinets(Integer maxPatinets) {
        this.maxPatinets = maxPatinets;
    }

    public Integer getMinPatients() {
        return minPatients;
    }

    public void setMinPatients(Integer minPatients) {
        this.minPatients = minPatients;
    }

    public Collection<Schedules> getSchedulesCollection() {
        return schedulesCollection;
    }

    public void setSchedulesCollection(Collection<Schedules> schedulesCollection) {
        this.schedulesCollection = schedulesCollection;
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
        if (!(object instanceof Shifts)) {
            return false;
        }
        Shifts other = (Shifts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.Shifts[ id=" + id + " ]";
    }
    
}
