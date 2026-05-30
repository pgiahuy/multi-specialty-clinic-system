/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.mapper.PaymentItemMapper;
import com.hb.service.PaymentItemsService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api")
public class ApiPaymentItemsController {
    @Autowired
    private PaymentItemsService itemService;
    
    @Autowired
    private PaymentItemMapper itemMapper;
    
//    @GetMapping("/secure/payment-items/{paymentId}")
//    public ResponseEntity<?> list(Principal principal, @PathVariable(value = "paymentId") Long paymentId,
//                                    @RequestParam Map<String, String> params) {
//        List<PaymentItems> items = itemService.getPaymentItemsByPaymentId(paymentId, params);
//        return ResponseEntity.ok(items.stream().map(itemMapper::toEntity).toList());
//    }
}
