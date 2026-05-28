/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import jakarta.security.enterprise.identitystore.openid.RefreshToken;

/**
 *
 * @author HUY
 */
public interface RefreshTokenService {
    public RefreshToken createRefreshToken(Long userId);
    public RefreshToken verifyExpiration(RefreshToken token);
    public void revokeByUserId(Long userId);
    
}
