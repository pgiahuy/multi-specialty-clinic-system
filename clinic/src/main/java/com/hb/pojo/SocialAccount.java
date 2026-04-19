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
@Table(name = "social_account")
@NamedQueries({
    @NamedQuery(name = "SocialAccount.findAll", query = "SELECT s FROM SocialAccount s"),
    @NamedQuery(name = "SocialAccount.findById", query = "SELECT s FROM SocialAccount s WHERE s.id = :id"),
    @NamedQuery(name = "SocialAccount.findByProvider", query = "SELECT s FROM SocialAccount s WHERE s.provider = :provider"),
    @NamedQuery(name = "SocialAccount.findByProviderId", query = "SELECT s FROM SocialAccount s WHERE s.providerId = :providerId")})
public class SocialAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 8)
    @Column(name = "provider")
    private String provider;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "provider_id")
    private String providerId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;

    public SocialAccount() {
    }

    public SocialAccount(Long id) {
        this.id = id;
    }

    public SocialAccount(Long id, String providerId) {
        this.id = id;
        this.providerId = providerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        if (!(object instanceof SocialAccount)) {
            return false;
        }
        SocialAccount other = (SocialAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.SocialAccount[ id=" + id + " ]";
    }
    
}
