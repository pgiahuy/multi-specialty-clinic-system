/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.response.AuthResponse;
import com.hb.pojo.RefreshToken;
import java.time.Instant;


/**
 *
 * @author HUY
 */
public interface RefreshTokenService {
    RefreshToken generateRefreshToken(Long userId, String deviceId, String deviceInfo, Instant oldExpiryDate);
    AuthResponse refresh(String token);
    void revokeLogout(String token);
    
}
