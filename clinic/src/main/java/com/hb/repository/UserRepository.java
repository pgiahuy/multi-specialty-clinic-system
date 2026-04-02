/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.User;
import java.util.List;

/**
 *
 * @author HUY
 */
public interface UserRepository{
    List<User> getUsers();
    User getUserByUsername(String username);
    User addUser(User u);
}
