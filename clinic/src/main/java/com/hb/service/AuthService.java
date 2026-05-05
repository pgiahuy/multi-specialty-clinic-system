/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.UserCreateRequest;

/**
 *
 * @author DELL
 */
public interface AuthService {
    void registerPatient(UserCreateRequest urq);
    boolean authenticate(String username, String password);
}
