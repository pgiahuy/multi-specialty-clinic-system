/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.AppointmentCreateRequest;
import com.hb.dto.response.AppointmentResponse;
import com.hb.mapper.AppointmentMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.User;

import com.hb.service.AppointmentService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@CrossOrigin
public class ApiAppointmentController {

    @Autowired
    private Environment env;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentMapper appMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/secure/appointments")
    public ResponseEntity<AppointmentResponse> register(@RequestBody AppointmentCreateRequest req) {
        AppointmentResponse a = this.appointmentService.addOrUpdateAppointment(req);
        return new ResponseEntity<>(a, HttpStatus.CREATED);
    }

    @GetMapping("/secure/appointments")
    public ResponseEntity<?> list(@RequestParam Map<String, String> params, Principal principal) {
        try {
            User u = userService.getUserByUsername(principal.getName());

            if (u != null) {
                params.put("currentUserId", String.valueOf(u.getId()));
                params.put("currentUserRole", u.getRole());
            }
            int pageSize = this.env.getProperty("admin.page_size", Integer.class, 10);
            params.put("pageSize", String.valueOf(pageSize));

            List<Appointment> res = appointmentService.getAppointments(params);
            return ResponseEntity.ok(res.stream().map(appMapper::toResponse).toList());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi hệ thống: " + e.getMessage());
        }
    }
    
   

    @PutMapping("/secure/appointments/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody AppointmentCreateRequest req,
            Principal principal) {
        try {
            User currentUser = userService.getUserByUsername(principal.getName());
            String role = currentUser.getRole();
            Appointment appoint = appointmentService.getAppointmentById(id);

            if (appoint == null) {
                return ResponseEntity.status(404).body("Không tìm thấy lịch hẹn!");
            }

            boolean isOwner = false;

            if ("ROLE_PATIENT".equals(role)) {
                isOwner = appoint.getPatientId().getUserId().getId().equals(currentUser.getId());
            } else if ("ROLE_DOCTOR".equals(role)) {
                isOwner = appoint.getScheduleId().getDoctorId().getUserId().getId().equals(currentUser.getId());
            }

            if (!isOwner) {
                return ResponseEntity.status(403).body("Không thể thay đổi lịch hẹn");
            }
            req.setAppId(id);
            AppointmentResponse updatedApp = this.appointmentService.addOrUpdateAppointment(req);
            return ResponseEntity.ok(updatedApp);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi cập nhật: " + e.getMessage());
        }
    }
}
