/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface UserRepository extends BaseRepository<User>{
    List<User> getUsers(Map<String, String> params);
    User getUserByUsername(String username);
    User getUserById(Long id);
    User getUserByEmail(String email);
    User saveOrUpdate(User u);
    void deleteUser(Long id);
    boolean authenticate(String username, String password);
    User existsByUsername(String username);
    User existsByEmail(String email);
}
