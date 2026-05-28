/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.response.AuthResponse;
import com.hb.pojo.RefreshToken;


/**
 *
 * @author HUY
 */
public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId);
    RefreshToken createOrUpdateRefreshToken(Long userId, String deviceId, String deviceInfo);
    RefreshToken verifyRefreshToken(String token);
    AuthResponse refresh(String token);
    void revokeByToken(String token);
    void revokeByUserAndDevice(Long userId, String deviceId);
    void revokeByRefreshToken(String refreshToken);
    
}
