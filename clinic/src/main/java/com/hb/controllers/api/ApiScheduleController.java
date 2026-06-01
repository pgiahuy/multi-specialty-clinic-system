/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.ScheduleCreateRequest;
import com.hb.dto.response.ScheduleRepsonse;
import com.hb.mapper.ScheduleMapper;
import com.hb.pojo.Schedule;
import com.hb.pojo.User;
import com.hb.service.ScheduleService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin
public class ApiScheduleController {

    @Autowired
    private Environment env;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @GetMapping("/secure/schedules")
    public ResponseEntity<List<ScheduleRepsonse>> list(@RequestParam Map<String, String> params, Principal principal) {

        User u = userService.getUserByUsername(principal.getName());
        
        if ("ROLE_DOCTOR".equals(u.getRole())) {
            if (u.getDoctor() != null) {
                params.put("doctorId", String.valueOf(u.getDoctor().getId()));
            }
        }

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));
        List<Schedule> s = scheduleService.getSchedules(params);
        return ResponseEntity.ok(s.stream().map(scheduleMapper::toResponse).toList());
    }

    @PostMapping("/secure/schedules")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ScheduleRepsonse> register(Principal principal, @RequestBody ScheduleCreateRequest req) {
        User u = this.userService.getUserByUsername(principal.getName());
        if (u.getDoctor() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Long doctorId = u.getDoctor().getId();

        req.setDoctorId(doctorId);
        ScheduleRepsonse s = scheduleService.addSchedule(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }
}
