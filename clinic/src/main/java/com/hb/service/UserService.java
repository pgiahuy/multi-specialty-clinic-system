/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author HUY
 */


public interface UserService extends UserDetailsService {
    User getUserByUsername(String username) ;
    User addUser(Map<String, String> params, MultipartFile avatar);
    List<User> getUsers(Map<String,String> params);
}
