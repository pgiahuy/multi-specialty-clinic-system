/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.LabResultCreateRequest;
import com.hb.dto.response.LabTestResponse;
import com.hb.dto.response.LabResultResponse;
import com.hb.enums.UserRole;
import com.hb.mapper.LabTestMapper;
import com.hb.mapper.LabResultMapper;
import com.hb.pojo.Appointment;
import com.hb.pojo.LabResult;
import com.hb.pojo.LabTest;
import com.hb.pojo.User;
import com.hb.service.AppointmentService;
import com.hb.service.LabTestService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hb.service.LabResultService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;

/**
 *
 * @author DELL
 */
@RestController
@CrossOrigin
@RequestMapping("/api/secure")
public class ApiLabResultController {
    
    @Autowired
    private LabTestService testService;

    @Autowired
    private LabResultService labResultService;

    @Autowired
    private LabResultMapper testResultMapper;

    @Autowired
    private UserService userService;
    
    @Autowired
    private LabResultMapper labResultMapper;
    
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/lab-results")
    public ResponseEntity<?> create(@RequestBody LabResultCreateRequest req, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Appointment appointment = appointmentService.getAppointmentById(req.getAppointmentId());
        
        boolean isOwner = false;
        
        if (UserRole.ROLE_DOCTOR.equals(currentUser.getRole())) {
            if (appointment.getScheduleId() != null && appointment.getScheduleId().getDoctorId() != null) {
                isOwner = appointment.getScheduleId().getDoctorId().getUserId().getId().equals(currentUser.getId());
            }
        }

        if (!isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền chỉ định xét nghiệm!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(labResultService.labTestOrder(req));
    }

    @GetMapping("/lab-results")
    public ResponseEntity<List<LabResultResponse>> list(@RequestParam Map<String, String> params, Principal principal) {
        User u = userService.getUserByUsername(principal.getName());

        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var patients = u.getPatientCollection(); 
        
        if (patients != null && !patients.isEmpty()) {        
            Long firstPatientId = patients.iterator().next().getId();
            params.put("patientId", String.valueOf(firstPatientId));
        }
        
        return ResponseEntity.ok(this.labResultService.getLabResults(params));
    }
    
    @GetMapping("lab-results/{id}")
    public ResponseEntity<?> getLabResult(@PathVariable(value="id") Long id){
        return ResponseEntity.ok(this.labResultService.getLabResultById(id));
    }
    
    
    @PutMapping("/lab-results/{id}")
    public ResponseEntity<?> update(@RequestBody LabResultCreateRequest req) {
        labResultService.addOrUpdateLabResult(req);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/lab-results/appointment/{appointmentId}")
    public ResponseEntity<LabResultResponse> list(Principal principal, @PathVariable(value = "appointmentId") Long appointmentId) {
        User u = userService.getUserByUsername(principal.getName());

        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(this.labResultService.getLabResultsesByAppointmentId(appointmentId));
    }
    
    
    @GetMapping("/tests")
    public ResponseEntity<?> list(@RequestParam Map<String, String> params) {
        List<LabTest> res = this.testService.getLabTests(params);
        long total = this.testService.countLabTests(params);
        
        
        List<LabTestResponse> testResponses = res.stream()
                .map(LabTestMapper.INSTANCE::toResponse)
                .toList();
        
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("tests", testResponses); 
        responseData.put("total", total);         
        
        return ResponseEntity.ok(responseData);
    }
    
    
    @GetMapping("/test/{id}")
    public ResponseEntity<LabTestResponse> getLabTest(@PathVariable(value="id") Long id) {
        LabTest res = testService.getLabTestById(id);
        return ResponseEntity.ok(LabTestMapper.INSTANCE.toResponse(res));
    }
    
    
}
