/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.pojo.Notification;
import com.hb.service.NotificationService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author HUY
 */
@RestController
@RequestMapping("/api/secure")
@PropertySource("classpath:configs.properties")
public class ApiNotificationController {

    @Autowired
    private NotificationService notiService;


    @Autowired
    private Environment env;

    @PatchMapping("/notifications/{id}/read")
    @ResponseStatus(HttpStatus.OK)
    public void markRead(@PathVariable("id") Long id, Principal principal) {
        System.out.println("heheh-controller-0");

        Notification n = this.notiService.getNotificationById(id);
        System.out.println("heheh-controller-1");

        if (n != null) {

            String currentUsername = principal.getName();

            if (n.getUserId().getUsername().equals(currentUsername)) {
                this.notiService.markAsRead(id);
            } else {

                throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thông báo!");
        }
        System.out.println("heheh-controller-OK");
    }
}
