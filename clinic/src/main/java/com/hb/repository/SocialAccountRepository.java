/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.SocialAccount;

/**
 *
 * @author HUY
 */
public interface SocialAccountRepository {
    SocialAccount findByProviderAndProviderId(String provider, String providerId);
    void save(SocialAccount socialAccount);
}
