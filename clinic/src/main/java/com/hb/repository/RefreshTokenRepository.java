/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.User;
import java.util.Optional;

/**
 *
 * @author HUY
 */
public interface RefreshTokenRepository {
//    Optional<RefreshToken> getByToken(String token);
    void deleteByUser(User u);
}
