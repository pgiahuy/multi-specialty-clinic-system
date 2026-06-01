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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "lab_test")
@NamedQueries({
    @NamedQuery(name = "LabTest.findAll", query = "SELECT l FROM LabTest l"),
    @NamedQuery(name = "LabTest.findById", query = "SELECT l FROM LabTest l WHERE l.id = :id"),
    @NamedQuery(name = "LabTest.findByTestName", query = "SELECT l FROM LabTest l WHERE l.testName = :testName"),
    @NamedQuery(name = "LabTest.findByUnit", query = "SELECT l FROM LabTest l WHERE l.unit = :unit"),
    @NamedQuery(name = "LabTest.findByNormalRange", query = "SELECT l FROM LabTest l WHERE l.normalRange = :normalRange"),
    @NamedQuery(name = "LabTest.findByPrice", query = "SELECT l FROM LabTest l WHERE l.price = :price")})
public class LabTest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "test_name")
    private String testName;
    @Size(max = 50)
    @Column(name = "unit")
    private String unit;
    @Size(max = 100)
    @Column(name = "normal_range")
    private String normalRange;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private BigDecimal price;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testId")
    private Collection<LabResultDetail> labResultDetailCollection;

    public LabTest() {
    }

    public LabTest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNormalRange() {
        return normalRange;
    }

    public void setNormalRange(String normalRange) {
        this.normalRange = normalRange;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
        if (!(object instanceof LabTest)) {
            return false;
        }
        LabTest other = (LabTest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.LabTest[ id=" + id + " ]";
    }
    
}
