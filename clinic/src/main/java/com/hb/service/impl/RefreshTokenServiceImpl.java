/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.response.AuthResponse;
import com.hb.exception.DuplicateResourceException;
import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.RefreshToken;
import com.hb.pojo.User;
import com.hb.repository.RefreshTokenRepository;
import com.hb.service.RefreshTokenService;
import com.hb.service.UserService;
import com.hb.utils.JwtUtils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepo;

    @Autowired
    private UserService userService;

    @Override
    public RefreshToken generateRefreshToken(Long userId, String deviceId, String deviceInfo,  Instant oldExpiryDate) {

        User user = userService.getUserById(userId);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUserId(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setRevoked(false);
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setDeviceId(deviceId); 
        refreshToken.setDeviceInfo(deviceInfo);
        
        if (oldExpiryDate == null) {
            refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        }else{
            refreshToken.setExpiryDate(oldExpiryDate );
        }
       
        return refreshTokenRepo.save(refreshToken);
    }

   
  

    @Override
    public AuthResponse refresh(String token) {

        RefreshToken rt = refreshTokenRepo.getByToken(token);
        if (rt == null) {
            throw new ResourceNotFoundException("Refresh token không tồn tại!");
        }
        
        Instant oldExpiryDate = rt.getExpiryDate();

        if(Boolean.TRUE.equals(rt.getRevoked())){
            refreshTokenRepo.revokeAllByUser(rt.getUserId().getId());
            throw new DuplicateResourceException("Refresh token đã thu hồi!");
        }


        if (rt.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepo.revokeByToken(token);
            throw new DuplicateResourceException("Refresh token đã hết hạn. Vui lòng đăng nhập lại!");
        }
        
        User user = rt.getUserId();

        RefreshToken newRt = generateRefreshToken(user.getId(), rt.getDeviceId(), rt.getDeviceInfo(), oldExpiryDate);
        String accessToken = JwtUtils.generateToken(user.getUsername(), user.getRole().toString());
        
        refreshTokenRepo.revokeByToken(token);

        return new AuthResponse(accessToken, newRt.getToken());
    }


    
    @Override
    public void revokeLogout(String token) {
        RefreshToken rt = refreshTokenRepo.getByToken(token);
        if (rt != null) {
            Long userId = rt.getUserId().getId();
            String deviceId = rt.getDeviceId();
            if (deviceId != null) {
                refreshTokenRepo.revokeByUserAndDevice(userId, deviceId);
            } else {
                refreshTokenRepo.revokeByToken(token);
            }
        }
    }



    

}
