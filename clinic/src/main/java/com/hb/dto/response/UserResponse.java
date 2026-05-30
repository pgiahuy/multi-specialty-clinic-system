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
public class UserResponse {
    private String username;
    private String name;
    private String email;
    private String avatar;
    private String role;
    private DoctorResponse doctorProfile;
    private List<PatientResponse> patientProfiles;

    public UserResponse() {
    }

    public UserResponse(String username, String avatar) {
        this.username = username;
        this.avatar = avatar;
    }
    
    
    
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public DoctorResponse getDoctorProfile() {
        return doctorProfile;
    }

    public void setDoctorProfile(DoctorResponse doctorProfile) {
        this.doctorProfile = doctorProfile;
    }

    public List<PatientResponse> getPatientProfiles() {
        return patientProfiles;
    }

    public void setPatientProfiles(List<PatientResponse> patientProfiles) {
        this.patientProfiles = patientProfiles;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
}
