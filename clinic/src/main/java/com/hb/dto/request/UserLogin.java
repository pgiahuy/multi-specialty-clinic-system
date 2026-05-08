/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;

/**
 *
 * @author HUY
 */
public class UserLogin {
    private String username;
    private String password;
    private String fcmToken;

    public UserLogin(String username, String password, String fcmToken) {
        this.username = username;
        this.password = password;
        this.fcmToken = fcmToken;
    }
    
    
    
    public UserLogin() {
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the fcmToken
     */
    public String getFcmToken() {
        return fcmToken;
    }

    /**
     * @param fcmToken the fcmToken to set
     */
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
    
    
    
    
}
