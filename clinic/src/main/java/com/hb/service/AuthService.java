/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.PatientCreateRequest;
import com.hb.dto.request.UserCreateRequest;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DELL
 */
public interface AuthService {
    void registerPatient(UserCreateRequest urq, PatientCreateRequest prq);
    boolean authenticate(String username, String password);
}
