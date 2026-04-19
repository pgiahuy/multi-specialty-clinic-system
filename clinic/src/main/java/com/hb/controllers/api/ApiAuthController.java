/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.hb.dto.request.PatientCreateRequest;
import com.hb.dto.request.UserCreateRequest;
import com.hb.enums.AuthProvider;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/auth")
@PropertySource("classpath:configs.properties")
public class ApiAuthController {

    @Autowired
    private Environment env;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    private final String CLIENT_ID = env.getProperty("CLIENT_ID", String.class);

    @PostMapping(value = "/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@ModelAttribute UserCreateRequest urq, @ModelAttribute PatientCreateRequest prq) {
        authService.registerPatient(urq, prq);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User u) {
        if (this.authService.authenticate(u.getUsername(), u.getPassword())) {
            try {
                String token = JwtUtils.generateToken(u.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> params) {
        String idTokenString = params.get("token");

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String googleId = payload.getSubject();

                User user = userService.processSocialLogin(email, name, googleId, AuthProvider.GOOGLE.name());

                String token = JwtUtils.generateToken(user.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/facebook")
    public ResponseEntity<?> loginWithFacebook(@RequestBody Map<String, String> body) {
        String accessToken = body.get("token");

        try {
            FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.LATEST);

            com.restfb.types.User fbUser = facebookClient.fetchObject("me", com.restfb.types.User.class,
                    Parameter.with("fields", "id,name,email"));

            if (fbUser != null) {
                String email = fbUser.getEmail();
                String name = fbUser.getName();
                String fbId = fbUser.getId();

                if (email == null) {
                    email = fbId + "@facebook.com";
                }

                User user = userService.processSocialLogin(email, name, fbId, AuthProvider.FACEBOOK.name());

                String token = JwtUtils.generateToken(user.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Facebook Token không hợp lệ");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
