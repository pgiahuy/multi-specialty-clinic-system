/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface VnpayPaymentService {
    String createVnPayPaymentUrl(Long billId, long amount, String bankCode, HttpServletRequest request);
    boolean verifyVnpaySignature(Map<String, String> params);
}
