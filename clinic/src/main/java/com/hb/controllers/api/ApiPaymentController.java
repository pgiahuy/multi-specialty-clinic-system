/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.enums.PaymentMethod;
import com.hb.mapper.PaymentMapper;
import com.hb.pojo.Patient;
import com.hb.pojo.Payment;
import com.hb.pojo.User;
import com.hb.repository.PaymentItemRepository;
import com.hb.service.MomoPaymentService;
import com.hb.service.PaymentItemsService;
import com.hb.service.PaymentService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@CrossOrigin
@RequestMapping("/api/secure")
public class ApiPaymentController {

    @Autowired
    private MomoPaymentService momoService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentMapper payMapper;

    @PostMapping("/payments/pay")
    public ResponseEntity<?> pay(
            @RequestParam("method") String method,
            @RequestParam("paymentId") Long paymentId,
            @RequestParam("orderInfo") String orderInfo) throws Exception {

        Long totalAmount = paymentService.getPaymentAmount(paymentId).longValue();
        String orderId = "ORDER_" + paymentId + "_" + System.currentTimeMillis();

        return switch (method.toUpperCase()) {
            case "MOMO" ->
                ResponseEntity.ok(momoService.createPayment(orderId, totalAmount, orderInfo));
            case "CASH" -> {
                Payment payment = paymentService.getPaymentById(paymentId);
                if (payment == null) {
                    
                    ResponseEntity.badRequest().body("Không tìm thấy hóa đơn!");
                }

                paymentService.confirmPaymentSuccess(paymentId, PaymentMethod.CASH);

               
                yield ResponseEntity.ok(Map.of("message", "Thanh toán thành công cập nhật DB", "status", "SUCCESS"));
            }

            default ->
                ResponseEntity.badRequest().body("Phương thức không hỗ trợ");
        };
    }

    @GetMapping("/payments/momo/return")
    public ResponseEntity<?> momoReturn(@RequestParam Map<String, String> params) throws Exception {

        boolean valid = momoService.verifySignature(params);
        String resultCode = params.get("resultCode");
        String orderId = params.get("orderId");
        String[] parts = orderId.split("_");
        Long paymentId = Long.parseLong(parts[1]);

        if (valid && "0".equals(resultCode)) {

            String extraData = params.get("extraData");

            if (extraData != null && !extraData.isEmpty()) {

                paymentService.confirmPaymentSuccess(paymentId, PaymentMethod.MOMO);
            }

        } else {

            paymentService.confirmPaymentFailed(paymentId, PaymentMethod.MOMO);
        }

        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/payments/momo/ipn")
    public ResponseEntity<?> momoIpn(@RequestBody Map<String, String> params) throws Exception {

        boolean valid = momoService.verifySignature(params);

        try {
            String resultCode = params.get("resultCode");
            String orderId = params.get("orderId");
            String[] parts = orderId.split("_");
            Long paymentId = Long.parseLong(parts[1]);

            if (valid && "0".equals(resultCode)) {

                String extraData = params.get("extraData");

                if (extraData != null && !extraData.isEmpty()) {

                    paymentService.confirmPaymentSuccess(paymentId, PaymentMethod.MOMO);

                }

            } else {

                paymentService.confirmPaymentFailed(paymentId, PaymentMethod.MOMO);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();

        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/payments/{patientId}")
    public ResponseEntity<?> list(Principal principal, @PathVariable(value = "patientId") Long patientId,
            @RequestParam Map<String, String> params) {
        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Collection<Patient> patients = u.getPatientCollection();

        List<Long> patientIds = patients.stream()
                .map(Patient::getId)
                .collect(Collectors.toList());

        if (!patientIds.contains(patientId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Payment> payments = this.paymentService.getPaymentByPatientId(patientId, params);

        return ResponseEntity.ok(payments.stream().map(payMapper::toResponse).toList());

    }
}
