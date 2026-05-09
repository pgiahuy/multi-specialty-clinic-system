/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.UserCreateRequest;
import com.hb.dto.response.NotificationResponse;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
