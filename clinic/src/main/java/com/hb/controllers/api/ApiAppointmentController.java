/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.AppointmentCreateRequest;
import com.hb.dto.response.AppointmentResponse;
import com.hb.mapper.AppointmentMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.Patient;
import com.hb.pojo.User;

import com.hb.service.AppointmentService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.Collection;
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
        AppointmentResponse a = this.appointmentService.registerAppointment(req);
        return new ResponseEntity<>(a, HttpStatus.CREATED);
    }

    @GetMapping("/secure/appointments")
    public ResponseEntity<?> list(@RequestParam Map<String, String> params, Principal principal) {
        User u = userService.getUserByUsername(principal.getName());

        if (u != null) {
            params.put("currentUserId", String.valueOf(u.getId()));
            params.put("currentUserRole", u.getRole());
        }
        int pageSize = this.env.getProperty("admin.page_size", Integer.class, 10);
        params.put("pageSize", String.valueOf(pageSize));

        List<Appointment> res = appointmentService.getAppointments(params);
        return ResponseEntity.ok(res.stream().map(appMapper::toResponse).toList());
    }


    @GetMapping("/secure/appointment/{id}")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable(value = "id") Long id) {
        Appointment res = this.appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appMapper.toResponse(res));
    }

    

   

    @PostMapping("/secure/appointments/{id}/confirm")
    public ResponseEntity<?> update(@PathVariable("id") Long id, Principal principal) {

        User currentUser = userService.getUserByUsername(principal.getName());
        Appointment appointment = appointmentService.getAppointmentById(id);

        if (appointment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy lịch hẹn!");
        }

        boolean isOwner = false;
        
        if ("ROLE_DOCTOR".equals(currentUser.getRole())) {
            if (appointment.getScheduleId() != null && appointment.getScheduleId().getDoctorId() != null) {
                isOwner = appointment.getScheduleId().getDoctorId().getUserId().getId().equals(currentUser.getId());
            }
        }

        if (!isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền xác nhận lịch hẹn!");
        }

        boolean confirmed = this.appointmentService.doctorConfirmAppointment(id);

        if (confirmed) {
            return ResponseEntity.ok("Xác nhận lịch hẹn thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xác nhận lịch hẹn thất bại hoặc lịch đã được xử lý trước đó!");
        }
    }
}
