/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.RefreshToken;

/**
 *
 * @author HUY
 */
public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    RefreshToken getByToken(String token);
    
    Boolean tokenIsExist(String token);
    void revokeByToken(String token);
    void revokeAllByUser(Long userId);
    void revokeByUserAndDevice(Long userId, String deviceId);
    
}
