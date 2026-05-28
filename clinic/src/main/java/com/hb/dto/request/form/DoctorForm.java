/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request.form;

import java.util.List;

/**
 *
 * @author HUY
 */
public class DoctorForm {
    private Long id;
    private String fullName;
    private String description;
    private String gender;
    private String cccd;
    private List<Long> specialtyIds;
    private Long userId;

    public DoctorForm() {
    }

    public DoctorForm(Long id, String fullName, String description, String gender, List<Long> specialtyIds, Long userId) {
        this.id = id;
        this.fullName = fullName;
        this.description = description;
        this.gender = gender;
        this.specialtyIds = specialtyIds;
        this.userId = userId;
    }
    
    

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the specialtyIds
     */
    public List<Long> getSpecialtyIds() {
        return specialtyIds;
    }

    /**
     * @param specialtyIds the specialtyIds to set
     */
    public void setSpecialtyIds(List<Long> specialtyIds) {
        this.specialtyIds = specialtyIds;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the cccd
     */
    public String getCccd() {
        return cccd;
    }

    /**
     * @param cccd the cccd to set
     */
    public void setCccd(String cccd) {
        this.cccd = cccd;
    }
    
    
    
}
