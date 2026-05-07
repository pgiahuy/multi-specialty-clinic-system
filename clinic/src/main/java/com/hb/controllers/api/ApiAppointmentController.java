/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.AppointmentCreateRequest;
import com.hb.dto.response.AppointmentResponse;
import com.hb.mapper.AppointmentMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.Doctor;
import com.hb.pojo.Patient;
import com.hb.pojo.User;

import com.hb.service.AppointmentService;
import com.hb.service.DoctorService;
import com.hb.service.PatientService;
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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HUY
 */
@RestController
@RequestMapping("/api")
@PropertySource("classpath:configs.properties")
public class ApiAppointmentController {

    @Autowired
    private Environment env;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentMapper appMapper;

    @Autowired
    private UserService userService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private DoctorService doctorService;
  

    @PostMapping("/secure/appointments")
    public ResponseEntity<AppointmentResponse> register(@RequestBody AppointmentCreateRequest req) {
        AppointmentResponse a = this.appointmentService.addAppointment(req);
        return new ResponseEntity<>(a, HttpStatus.CREATED);
    }

    @GetMapping("/secure/appointments")
    public ResponseEntity<List<AppointmentResponse>> list(@RequestParam Map<String, String> params, Principal principal) {
        User u = userService.getUserByUsername(principal.getName());

        if (u != null) {
            params.put("currentUserId", String.valueOf(u.getId()));
            params.put("currentUserRole", u.getRole());
            System.out.println("=============API APPOINT============");
            System.out.println(u.getId());
            System.out.println(u.getRole());
        }
        int pageSize = this.env.getProperty("admin.page_size", Integer.class, 10);
        params.put("pageSize", String.valueOf(pageSize));

        List<Appointment> res = appointmentService.getAppointments(params);
        return ResponseEntity.ok(res.stream().map(appMapper::toResponse).toList());
    }

}
