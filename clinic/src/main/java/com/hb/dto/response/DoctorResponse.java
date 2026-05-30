/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

import java.util.List;

/**
 *
 * @author DELL
 */
public class DoctorResponse {
    private Long id;
    private String avatar;
    private String fullName;
    private String description;
    private List<SpecialtyResponse> specialtiesOfDoctor;
    private String email;
    private String gender;

    public DoctorResponse() {
    }

    public DoctorResponse(Long id, String avatar, String fullName, String description, List<SpecialtyResponse> specialtiesOfDoctor, String email, String gender) {
        this.id = id;
        this.avatar = avatar;
        this.fullName = fullName;
        this.description = description;
        this.specialtiesOfDoctor = specialtiesOfDoctor;
        this.email = email;
        this.gender = gender;
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
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
     * @return the specialtiesOfDoctor
     */
    public List<SpecialtyResponse> getSpecialtiesOfDoctor() {
        return specialtiesOfDoctor;
    }

    /**
     * @param specialtiesOfDoctor the specialtiesOfDoctor to set
     */
    public void setSpecialtiesOfDoctor(List<SpecialtyResponse> specialtiesOfDoctor) {
        this.specialtiesOfDoctor = specialtiesOfDoctor;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
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

    
    
}
