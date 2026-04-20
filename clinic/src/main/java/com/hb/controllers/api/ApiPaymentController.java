/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.service.MomoPaymentService;
import com.hb.service.PaymentService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api/payment")
public class ApiPaymentController {

    @Autowired
    private MomoPaymentService momoService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(
            @RequestParam("method") String method, // Thêm "method"
            @RequestParam("orderId") String orderId, // Thêm "orderId"
            @RequestParam("amount") long amount, // Thêm "amount"
            @RequestParam("orderInfo") String orderInfo) throws Exception {

        return switch (method.toUpperCase()) {
            case "MOMO" ->
                ResponseEntity.ok(momoService.createPayment(orderId, amount, orderInfo));
//            case "VNPAY"   -> ResponseEntity.ok(vnpayService.createPayment(orderId, amount, orderInfo));
//            case "ZALOPAY" -> ResponseEntity.ok(zaloPayService.createPayment(orderId, amount, orderInfo));
//            case "COD"     -> ResponseEntity.ok(Map.of("method", "COD", "orderId", orderId));
            default ->
                ResponseEntity.badRequest().body("Phương thức không hỗ trợ");
        };
    }

    @GetMapping("/momo/return")
    public ResponseEntity<?> momoReturn(@RequestParam Map<String, String> params) throws Exception {
        boolean valid = momoService.verifySignature(params);
        String resultCode = params.get("resultCode");

        // Nếu thanh toán thành công qua link redirect của trình duyệt
        if (valid && "0".equals(resultCode)) {
            Long orderId = Long.parseLong(params.get("orderId"));
            paymentService.confirmPaymentSuccess(orderId, params.get("transId"));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("method", "MOMO");
        result.put("success", valid && "0".equals(resultCode));
        result.put("orderId", params.getOrDefault("orderId", ""));
        result.put("transId", params.getOrDefault("transId", ""));
        result.put("message", params.getOrDefault("message", ""));
        result.put("amount", params.getOrDefault("amount", ""));

        return ResponseEntity.ok(result);
    }

    @PostMapping("/momo/ipn")
    public ResponseEntity<?> momoIpn(@RequestBody Map<String, String> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            boolean valid = momoService.verifySignature(params);
            String resultCode = params.get("resultCode");

            if (valid && "0".equals(resultCode)) {
                // Thanh toán thành công qua kênh ngầm (Chuẩn nhất)
                Long orderId = Long.parseLong(params.get("orderId"));
                String transId = params.get("transId");

                // GỌI SERVICE CẬP NHẬT DB TẠI ĐÂY
                paymentService.confirmPaymentSuccess(orderId, transId);

                result.put("resultCode", 0);
                result.put("message", "Success");
            } else {
                result.put("resultCode", 1);
                result.put("message", "Failed");
            }
        } catch (Exception e) {
            result.put("resultCode", 1);
            result.put("message", "Error: " + e.getMessage());
        }

        return ResponseEntity.ok(result);
    }
}
