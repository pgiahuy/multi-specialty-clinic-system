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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author HUY
 */
@Entity
@Table(name = "refresh_token")
@NamedQueries({
    @NamedQuery(name = "RefreshToken.findAll", query = "SELECT r FROM RefreshToken r"),
    @NamedQuery(name = "RefreshToken.findById", query = "SELECT r FROM RefreshToken r WHERE r.id = :id"),
    @NamedQuery(name = "RefreshToken.findByToken", query = "SELECT r FROM RefreshToken r WHERE r.token = :token"),
    @NamedQuery(name = "RefreshToken.findByExpiryDate", query = "SELECT r FROM RefreshToken r WHERE r.expiryDate = :expiryDate"),
    @NamedQuery(name = "RefreshToken.findByCreatedAt", query = "SELECT r FROM RefreshToken r WHERE r.createdAt = :createdAt"),
    @NamedQuery(name = "RefreshToken.findByRevoked", query = "SELECT r FROM RefreshToken r WHERE r.revoked = :revoked")})
public class RefreshToken implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "token")
    private String token;
    @Basic(optional = false)
    @NotNull
    @Column(name = "expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime expiryDate;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Column(name = "revoked")
    private Boolean revoked;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public RefreshToken() {
    }

    public RefreshToken(Long id) {
        this.id = id;
    }

    public RefreshToken(Long id, String token, LocalDateTime expiryDate) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof RefreshToken)) {
            return false;
        }
        RefreshToken other = (RefreshToken) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hb.pojo.RefreshToken[ id=" + id + " ]";
    }
    
}
