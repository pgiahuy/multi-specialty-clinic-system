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
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "lab_tests")
@NamedQueries({
    @NamedQuery(name = "LabTests.findAll", query = "SELECT l FROM LabTests l"),
    @NamedQuery(name = "LabTests.findById", query = "SELECT l FROM LabTests l WHERE l.id = :id"),
    @NamedQuery(name = "LabTests.findByTestName", query = "SELECT l FROM LabTests l WHERE l.testName = :testName"),
    @NamedQuery(name = "LabTests.findByUnit", query = "SELECT l FROM LabTests l WHERE l.unit = :unit"),
    @NamedQuery(name = "LabTests.findByNormalRange", query = "SELECT l FROM LabTests l WHERE l.normalRange = :normalRange")})
public class LabTests implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "test_name")
    private String testName;
    @Size(max = 50)
    @Column(name = "unit")
    private String unit;
    @Size(max = 100)
    @Column(name = "normal_range")
    private String normalRange;
    @Column(name = "price")
    private BigDecimal price;
    @OneToMany(mappedBy = "testId")
    private Collection<LabResults> labResultsCollection;
    @OneToMany(mappedBy = "labTest")
    private Collection<PaymentItems> paymentItemsCollection;

    public LabTests() {
    }

    public LabTests(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Collection<LabResults> getLabResultsCollection() {
        return labResultsCollection;
    }

    public void setLabResultsCollection(Collection<LabResults> labResultsCollection) {
        this.labResultsCollection = labResultsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LabTests)) {
            return false;
        }
        LabTests other = (LabTests) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.LabTests[ id=" + getId() + " ]";
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the paymentItemsCollection
     */
    public Collection<PaymentItems> getPaymentItemsCollection() {
        return paymentItemsCollection;
    }

    /**
     * @param paymentItemsCollection the paymentItemsCollection to set
     */
    public void setPaymentItemsCollection(Collection<PaymentItems> paymentItemsCollection) {
        this.paymentItemsCollection = paymentItemsCollection;
    }

}
