/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.pojo;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "specialtie")
@NamedQueries({
    @NamedQuery(name = "Specialtie.findAll", query = "SELECT s FROM Specialtie s"),
    @NamedQuery(name = "Specialtie.findById", query = "SELECT s FROM Specialtie s WHERE s.id = :id"),
    @NamedQuery(name = "Specialtie.findByName", query = "SELECT s FROM Specialtie s WHERE s.name = :name")})
public class Specialtie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSpecailty", fetch = FetchType.LAZY)
    private Collection<Doctor> doctorCollection;
    @JoinColumn(name = "id_hod", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Doctor idHod;

    public Specialtie() {
    }

    public Specialtie(Long id) {
        this.id = id;
    }

    public Specialtie(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Doctor> getDoctorCollection() {
        return doctorCollection;
    }

    public void setDoctorCollection(Collection<Doctor> doctorCollection) {
        this.doctorCollection = doctorCollection;
    }

    public Doctor getIdHod() {
        return idHod;
    }

    public void setIdHod(Doctor idHod) {
        this.idHod = idHod;
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
        if (!(object instanceof Specialtie)) {
            return false;
        }
        Specialtie other = (Specialtie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.Specialtie[ id=" + id + " ]";
    }
    
}
