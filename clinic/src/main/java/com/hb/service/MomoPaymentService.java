/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.dto.response.MoMoPaymentResponse;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface MomoPaymentService {
    MoMoPaymentResponse createPayment(String orderId, long amount, String orderInfo,  String extraData) throws Exception;
    boolean verifySignature(Map<String, String> params) throws Exception;
}
