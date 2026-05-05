/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.response.MoMoPaymentResponse;
import com.hb.pojo.PaymentItems;
import com.hb.repository.PaymentItemRepository;
import com.hb.service.MomoPaymentService;
import com.hb.service.PaymentItemsService;
import com.hb.service.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    @Autowired
    private PaymentItemsService paymentItemSer;

    @Autowired
    private PaymentItemRepository itemRepo;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(
            @RequestParam("method") String method,
            @RequestParam("itemIds") List<Long> itemIds,
            @RequestParam("orderInfo") String orderInfo) throws Exception {

        //Payment p = paymentService.getPaymentById(Long.parseLong(paymentId));
        Long totalAmount = paymentService.calculateTotalFee(itemIds);
        String uniqueOrderId = "ORDER_" + System.currentTimeMillis();

        // Chuyển [1,2,3] thành "1,2,3"
        String extraData = itemIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        return switch (method.toUpperCase()) {
            case "MOMO" ->
                ResponseEntity.ok(momoService.createPayment(uniqueOrderId, totalAmount, orderInfo, extraData));
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

        if (valid && "0".equals(resultCode)) {
            String extraData = params.get("extraData");
            if (extraData != null && !extraData.isEmpty()) {
                List<Long> itemIds = Arrays.stream(extraData.split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                String transId = params.get("transId");

                paymentItemSer.confirmItemsPaid(transId, "MOMO", itemIds);
            }
        } 

        MoMoPaymentResponse response = new MoMoPaymentResponse();
        response.setMessage(params.get("message"));

        response.setAmount(Long.parseLong(params.get("amount")));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/momo/ipn")
    public ResponseEntity<?> momoIpn(@RequestBody Map<String, String> params) throws Exception {
        
        boolean valid = momoService.verifySignature(params);
        Map<String, Object> result = new HashMap<>();
        try {
            String resultCode = params.get("resultCode");
            // Giả sử valid = true để test
            if (valid && "0".equals(resultCode)) {
                String transId = params.get("transId");
                String extraData = params.get("extraData");

                if (extraData != null && !extraData.isEmpty()) {
                    List<Long> itemIds = Arrays.stream(extraData.split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());

                    // Tìm item đầu tiên để lấy PaymentID cha
                    PaymentItems firstItem = itemRepo.getItemById(itemIds.get(0));

                    if (firstItem != null) {
                        // Lấy ID của hóa đơn tổng
                        Long paymentIdInDb = firstItem.getPaymentId().getId();

                        // Gọi hàm confirm để cập nhật trạng thái của cả hóa đơn và các item liên quan
                        paymentService.confirmPaymentSuccess(paymentIdInDb, transId, "MOMO", itemIds);
                    }
                }
                result.put("resultCode", 0);
                result.put("message", "Success");
            }
        } catch (Exception e) {
            result.put("resultCode", 1);
            result.put("message", "Error: " + e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}
