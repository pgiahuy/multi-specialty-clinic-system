/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.hb.dto.request.UserCreateRequest;
import com.hb.dto.request.UserLogin;
import com.hb.enums.AuthProvider;
import com.hb.exception.DuplicateResourceException;
import com.hb.pojo.User;
import com.hb.service.AuthService;
import com.hb.service.UserService;
import com.hb.utils.JwtUtils;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api")
@PropertySource("classpath:configs.properties")
@CrossOrigin
public class ApiAuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private GoogleIdTokenVerifier verifier;

    @PostMapping(value = "/auth/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@ModelAttribute UserCreateRequest urq) {
        try {
            authService.registerPatient(urq);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserLogin u) {
        if (this.authService.authenticate(u.getUsername(), u.getPassword())) {
            try {

                if (u.getFcmToken() != null && !u.getFcmToken().isEmpty()) {
                    this.userService.updateFcmToken(u.getUsername(), u.getFcmToken());
                }

                String role = this.userService.getRoleByUsername(u.getUsername());
                String token = JwtUtils.generateToken(u.getUsername(), role);

                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi hệ thống khi xử lý đăng nhập");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }

    @PostMapping("/auth/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> params) {
        String idTokenString = params.get("token");
        String fcmToken = params.get("fcmToken");

        if (idTokenString == null || idTokenString.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token không được để trống");
        }

        try {

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            User user = userService.processSocialLogin(payload,fcmToken);

            String role = userService.getRoleByUsername(user.getUsername());

            return ResponseEntity.ok(Collections.singletonMap("token", JwtUtils.generateToken(user.getEmail(), role)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
