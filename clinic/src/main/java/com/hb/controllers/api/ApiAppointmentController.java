/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.AppointmentCreateRequest;
import com.hb.dto.response.AppointmentResponse;

import com.hb.service.AppointmentService;
import com.hb.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HUY
 */

@RestController
@RequestMapping("/api")
public class ApiAppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService userService;
    
    @PostMapping("/secure/appointments")
    public ResponseEntity<AppointmentResponse> register(@RequestBody AppointmentCreateRequest req){
        AppointmentResponse a = this.appointmentService.addAppointment(req);
        return new ResponseEntity<>(a, HttpStatus.CREATED);
    }
    
//    @GetMapping("/appointments/specialtys/{id}")
//    public ResponseEntity<List<AppointmentResponse>> getBySpecialtyId(@PathVariable Long id){
//        
//    }
//            
}
