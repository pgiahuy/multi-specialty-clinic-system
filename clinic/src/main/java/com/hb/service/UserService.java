/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.UserCreateRequest;
import com.hb.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author HUY
 */


public interface UserService extends UserDetailsService {
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User addUser(UserCreateRequest urq);
    List<User> getUsers(Map<String,String> params);
    void deleteUser(Long id);
    User processSocialLogin(String email, String name, String providerId, String providerName);
    long countUsers(Map<String, String> params);
    void updateFcmToken(String username, String fcmToken);
    String getRoleByUsername(String username);
}
