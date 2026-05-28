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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public RefreshToken createRefreshToken(Long userId) {

        User user = userService.getUserById(userId);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUserId(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setRevoked(false);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));

        return refreshTokenRepo.save(refreshToken);
    }

    @Override
    public RefreshToken createOrUpdateRefreshToken(Long userId, String deviceId, String deviceInfo) {

        User user = userService.getUserById(userId);

        // try to find existing token for this device
        RefreshToken existing = refreshTokenRepo.findByUserIdAndDeviceId(userId, deviceId);

        if (existing != null) {
            existing.setToken(UUID.randomUUID().toString());
            existing.setRevoked(false);
            existing.setCreatedAt(Instant.now());
            existing.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
            existing.setDeviceInfo(deviceInfo);
            return refreshTokenRepo.save(existing);
        }

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUserId(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setRevoked(false);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshToken.setDeviceId(deviceId);
        refreshToken.setDeviceInfo(deviceInfo);

        return refreshTokenRepo.save(refreshToken);
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepo.getByToken(token);

        if (refreshToken == null) {
            throw new ResourceNotFoundException("Refresh token không tồn tại!");
        }
        
        if (refreshToken.getRevoked() == true) {
            throw new DuplicateResourceException("Refresh token đã thu hồi!");
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepo.revokeByToken(token);
            throw new DuplicateResourceException("Refresh token đã hết hạn!");
        }

        return refreshToken;
    }

    @Override
    public AuthResponse refresh(String token){

        RefreshToken rt = refreshTokenRepo.getByToken(token);

        if (rt == null) {
            throw new ResourceNotFoundException("Refresh token không tồn tại!");
        }


        if (rt.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepo.revokeByToken(token);
            throw new DuplicateResourceException("Refresh token đã hết hạn!");
        }

        int updated = refreshTokenRepo.revokeIfNotRevoked(token);

        if (updated == 0) {
            Long userId = rt.getUserId().getId();
            refreshTokenRepo.revokeAllByUser(userId);
            throw new DuplicateResourceException("Refresh token đã hết hạn!");
        }

        User user = rt.getUserId();

        RefreshToken newRt = createOrUpdateRefreshToken(user.getId(), rt.getDeviceId(), rt.getDeviceInfo());
        String accessToken = JwtUtils.generateToken(user.getUsername(), user.getRole());

        return new AuthResponse(accessToken, newRt.getToken());
    }

    @Override
    public void revokeByToken(String token) {
        this.refreshTokenRepo.revokeByToken(token);
    }

    @Override
    public void revokeByUserAndDevice(Long userId, String deviceId) {
        this.refreshTokenRepo.revokeByUserAndDevice(userId, deviceId);
    }

    @Override
    public void revokeByRefreshToken(String refreshToken) {
        RefreshToken rt = refreshTokenRepo.getByToken(refreshToken);
        if (rt != null) {
            Long userId = rt.getUserId().getId();
            String deviceId = rt.getDeviceId();
            if (deviceId != null) {
                refreshTokenRepo.revokeByUserAndDevice(userId, deviceId);
            } else {

                refreshTokenRepo.revokeByToken(refreshToken);
            }
        }
    }

}
