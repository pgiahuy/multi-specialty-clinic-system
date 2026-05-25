/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.PaymentResponse;
import com.hb.pojo.Payment;
import org.springframework.stereotype.Component;

/**
 *
 * @author DELL
 */
@Component
public class PaymentMapper {
    public PaymentResponse toResponse(Payment p) {
        PaymentResponse res = new PaymentResponse();
        res.setId(p.getId());
        res.setCreatedDate(p.getCreatedAt());
        res.setTotalAmount(p.getTotalAmount());
        
        if (p.getPatientId() != null) {
            res.setPatientName(p.getPatientId().getFullName());
            res.setAddress(p.getPatientId().getAddress());
        }
        return res;
    }
}
