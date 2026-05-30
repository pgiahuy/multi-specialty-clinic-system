/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.response.DoctorResponse;
import com.hb.dto.response.PatientResponse;
import com.hb.dto.response.NotificationResponse;
import com.hb.dto.response.UserResponse;
import com.hb.mapper.DoctorMapper;
import com.hb.mapper.PatientMapper;
import com.hb.mapper.NotificationMapper;
import com.hb.pojo.Notification;
import com.hb.pojo.User;
import com.hb.service.NotificationService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HUY
 */
@RestController
@RequestMapping("/api/secure")
@PropertySource("classpath:configs.properties")
public class ApiUserController {

    @Autowired
    private NotificationService notiService;

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private Environment env;

    @GetMapping("users/notifications")
    public ResponseEntity<List<NotificationResponse>> list(@RequestParam Map<String, String> params, Principal principal) {
        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));

        User u = this.userService.getUserByUsername(principal.getName());

        if (u == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        params.put("userId", u.getId().toString());
        List<Notification> notis = this.notiService.getNotificationsByUserId(params);

        return ResponseEntity.ok(notis.stream().map(NotificationMapper.INSTANCE::toResponse).toList());
    }

    @GetMapping("/users/profile")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<UserResponse> getProfile(Principal principal) {
        User user = this.userService.getUserByUsername(principal.getName());
        UserResponse resp = new UserResponse();
        
        resp.setUsername(user.getUsername());
        resp.setEmail(user.getEmail());
        resp.setAvatar(user.getSecureUrl());
        resp.setRole(user.getRole());
        resp.setName(user.getName());

        if (user.getDoctor() != null) {
            DoctorResponse doctorProfile = doctorMapper.toResponse(user.getDoctor());
            resp.setDoctorProfile(doctorProfile);
        }

        if (user.getPatientCollection() != null) {
            List<PatientResponse> patientProfiles = user.getPatientCollection()
                    .stream()
                    .map(patientMapper::toResponse)
                    .toList();
            resp.setPatientProfiles(patientProfiles);
        }

        return new ResponseEntity<>(resp, HttpStatus.OK);
        
    }
}
