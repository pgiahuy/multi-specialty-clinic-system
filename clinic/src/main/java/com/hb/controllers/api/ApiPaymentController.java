/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.enums.PaymentMethod;
import com.hb.enums.UserRole;
import com.hb.mapper.PaymentMapper;
import com.hb.pojo.Patient;
import com.hb.pojo.Payment;
import com.hb.pojo.User;
import com.hb.repository.PatientRepository;
import com.hb.service.MomoPaymentService;
import com.hb.service.PaymentService;
import com.hb.service.UserService;
import com.hb.service.VnpayPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PropertySource("classpath:configs.properties")
public class ApiPaymentController {

    @Autowired
    private MomoPaymentService momoService;

    @Autowired
    private VnpayPaymentService vnpayService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentMapper payMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientRepository patientRepo;

//    @Autowired
//    private PaymentMapper payMapper;
//    
    @Autowired
    private Environment env;
    
    
    
    @GetMapping("/payments")
    @PreAuthorize("hasAnyRole('PATIENT','STAFF')")
    public ResponseEntity<?> list(@RequestParam Map<String,String> params, Principal principal) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));
        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (u.getRole() == UserRole.ROLE_PATIENT) {
            if (params.containsKey("patientId")) {
                Long requestedPatientId = Long.valueOf(params.get("patientId"));
                boolean ownsPatient = patientRepo.getPatientsByUserId(u.getId()).stream()
                        .anyMatch(p -> p.getId().equals(requestedPatientId));
                if (!ownsPatient) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền truy cập hóa đơn của bệnh nhân này");
                }
            }
            if (!params.containsKey("patientId")) {
                params.put("username", u.getUsername());
                return ResponseEntity.ok(paymentService.getPaymentsByUserName(params));
            }
            return ResponseEntity.ok(paymentService.getPayments(params));
        }

        if (u.getRole() == UserRole.ROLE_STAFF) {
            return ResponseEntity.ok(paymentService.getPayments(params));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @PostMapping("/payments/pay")
    @PreAuthorize("hasAnyRole('PATIENT','STAFF')")
    public ResponseEntity<?> pay(
            Principal principal,
            @RequestParam("method") String method,
            @RequestParam("paymentId") Long paymentId,
            @RequestParam("orderInfo") String orderInfo,
            HttpServletRequest request) throws Exception {

        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (u.getRole() == UserRole.ROLE_PATIENT) {
            if ("CASH".equalsIgnoreCase(method)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không cho phép thanh toán tiền mặt từ patient.");
            }
            Payment payment = paymentService.getPaymentById(paymentId);
            if (payment == null || payment.getAppointmentId() == null || payment.getAppointmentId().getPatientId() == null
                    || payment.getAppointmentId().getPatientId().getUserId() == null
                    || !payment.getAppointmentId().getPatientId().getUserId().getId().equals(u.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền thanh toán hóa đơn này");
            }
        }

        Long totalAmount = paymentService.getPaymentAmount(paymentId).longValue();
        String orderId = "ORDER_" + paymentId + "_" + System.currentTimeMillis();

        return switch (method.toUpperCase()) {
            case "MOMO" ->
                ResponseEntity.ok(momoService.createPayment(orderId, totalAmount, orderInfo));
            case "VNPAY" -> {
                String vnpayUrl = vnpayService.createVnPayPaymentUrl(paymentId, totalAmount, "", request);
                yield ResponseEntity.ok(Map.of("payUrl", vnpayUrl));
            }
            case "CASH" -> {
                Payment payment = paymentService.getPaymentById(paymentId);
                if (payment == null) {

                    yield ResponseEntity.badRequest().body("Không tìm thấy hóa đơn!");
                }
                paymentService.confirmPaymentSuccess(paymentId, PaymentMethod.CASH);

                yield ResponseEntity.ok(Map.of("message", "Thanh toán thành công cập nhật DB", "status", "SUCCESS"));
            }

            default ->
                ResponseEntity.badRequest().body("Phương thức không hỗ trợ");
        };
    }

    @GetMapping("/payments/appointment/{appointmentId}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getPaymentByAppointment(Principal principal,
            @PathVariable("appointmentId") Long appointmentId) {

        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Payment payment = paymentService.getPaymentByAppoint(appointmentId);
        if (payment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hóa đơn cho lịch hẹn này");
        }
        
        if (u.getRole() == UserRole.ROLE_PATIENT) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền truy cập!");
        }

        return ResponseEntity.ok(payMapper.toResponse(payment));
    }

    public ResponseEntity<?> momoReturn(@RequestParam Map<String, String> params) throws Exception {
        boolean valid = momoService.verifySignature(params);
        String resultCode = params.get("resultCode");
        String orderId = params.get("orderId");
        Long paymentId = Long.parseLong(orderId.split("_")[1]);

        if (valid && "0".equals(resultCode)) {
            paymentService.confirmPaymentSuccess(paymentId, PaymentMethod.MOMO);
        } else {
            paymentService.confirmPaymentFailed(paymentId, PaymentMethod.MOMO);
        }
        return ResponseEntity.ok().body(null);
    }

    public ResponseEntity<?> momoIpn(@RequestBody Map<String, String> params) throws Exception {
        boolean valid = momoService.verifySignature(params);
        try {
            String resultCode = params.get("resultCode");
            String orderId = params.get("orderId");
            Long paymentId = Long.parseLong(orderId.split("_")[1]);

            if (valid && "0".equals(resultCode)) {
                paymentService.confirmPaymentSuccess(paymentId, PaymentMethod.MOMO);
            } else {
                paymentService.confirmPaymentFailed(paymentId, PaymentMethod.MOMO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/payments/vnpay/ipn")
    public ResponseEntity<?> vnpayIpn(@RequestParam Map<String, String> params) {
        try {
            String vnp_ResponseCode = params.get("vnp_ResponseCode");

            String orderInfo = params.get("vnp_OrderInfo");
            Long paymentId = Long.parseLong(orderInfo.replaceAll("[^0-9]", ""));

            Map<String, String> response = new HashMap<>();

            if (!vnpayService.verifyVnpaySignature(params)) {
               
                response.put("RspCode", "97");
                response.put("Message", "Invalid Checksum");
                return ResponseEntity.ok(response);
            }

            if ("00".equals(vnp_ResponseCode)) {
                paymentService.confirmPaymentSuccess(paymentId, PaymentMethod.VNPAY);

                response.put("RspCode", "00");
                response.put("Message", "Confirm Success");
            } else {
                paymentService.confirmPaymentFailed(paymentId, PaymentMethod.VNPAY);

                response.put("RspCode", "01");
                response.put("Message", "Payment Failed");
            }
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("RspCode", "99");
            response.put("Message", "Unknow error");
            return ResponseEntity.ok(response);
        }
    }

    
}
