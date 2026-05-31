/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hb.enums.PatientRelationship;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author DELL
 */
public class PatientResponse {
    private Long id;
    private String cccd;
    private String fullName;
    private String phone;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob;
    private String address;
    private String gender;
    private PatientRelationship relationship;

    public PatientResponse() {
    }

    public PatientResponse(Long id, String cccd, String fullName, String phone, LocalDate dob, String address, String gender, PatientRelationship relationship) {
        this.id = id;
        this.cccd = cccd;
        this.fullName = fullName;
        this.phone = phone;
        this.dob = dob;
        this.address = address;
        this.gender = gender;
        this.relationship = relationship;
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
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the dob
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
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
     * @return the relationship
     */
    public PatientRelationship getRelationship() {
        return relationship;
    }

    /**
     * @param relationship the relationship to set
     */
    public void setRelationship(PatientRelationship relationship) {
        this.relationship = relationship;
    }
}
