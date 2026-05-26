/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;

import java.time.LocalDate;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DELL
 */
public class PatientCreateRequest {
    private String cccd;
    private String fullName;
    private String gender;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob;
    private String phone;
    private String address;
    private MultipartFile avatar;

    public PatientCreateRequest() {
    }

    public PatientCreateRequest(String cccd, String fullName, String gender, LocalDate dob, String phone,String address, MultipartFile avatar) {
        this.cccd = cccd;
        this.fullName = fullName;
        this.gender = gender;
        this.dob = dob;
        this.phone = phone;
        this.avatar = avatar;
        this.address = address;
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
     * @return the avatar
     */
    public MultipartFile getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
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
