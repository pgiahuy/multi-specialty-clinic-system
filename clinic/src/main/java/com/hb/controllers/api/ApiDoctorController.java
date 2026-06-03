/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.response.DoctorResponse;
import com.hb.dto.response.ScheduleRepsonse;
import com.hb.mapper.DoctorMapper;
import com.hb.mapper.ScheduleMapper;
import com.hb.pojo.Doctor;
import com.hb.pojo.User;
import com.hb.service.DoctorService;
import com.hb.service.ScheduleService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api")
@PropertySource("classpath:configs.properties")
public class ApiDoctorController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private UserService userService;
    @Autowired
    private DoctorMapper doctorMapp;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private Environment env;

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> list(@RequestParam Map<String, String> params) {

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));
        List<Doctor> doctors = this.doctorService.getDoctors(params);

        return ResponseEntity.ok(doctors.stream().map(this.doctorMapp::toResponse).toList());

    }
    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<DoctorResponse> retrieve(@PathVariable(value = "doctorId") Long id) {
        Doctor d = doctorService.getDoctorById(id);

        return ResponseEntity.ok(doctorMapp.toResponse(d));
    }
    
    
    @GetMapping("/secure/doctors/schedules/{scheduleId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ScheduleRepsonse> getSchedule(@PathVariable(value = "scheduleId") Long id , Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        Map<String,String> params = new HashMap<>();
        if (id != null && user.getDoctor() != null) {
            params.put("scheduleId", id.toString());
            params.put("doctorId", user.getDoctor().getId().toString());
        }
        return ResponseEntity.ok(scheduleMapper.toResponse(this.scheduleService.getScheduleByDoctor(params)));
    }

}
