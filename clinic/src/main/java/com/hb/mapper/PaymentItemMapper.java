/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.PaymentItemResponse;
import com.hb.pojo.PaymentItems;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 *
 * @author DELL
 */
@Component
public class PaymentItemMapper {
    public PaymentItemResponse toEntity(PaymentItems item){
        PaymentItemResponse res = new PaymentItemResponse();
        res.setId(item.getId());
        res.setType(item.getItemType());
        res.setCreatedAt(item.getCreatedAt());
        res.setAmount(item.getAmount());
        res.setPaidAt(item.getPaidAt());
        res.setMethod(item.getMethod());
        res.setStatus(item.getStatus());
        res.setStransId(item.getTransId());
        if(item.getLabTestId() != null) {
            res.setTestName(item.getLabTestId().getTestName());
        }
        return res;
    }
}
