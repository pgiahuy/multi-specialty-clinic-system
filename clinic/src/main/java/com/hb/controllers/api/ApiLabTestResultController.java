/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.LabResultCreateRequest;
import com.hb.dto.request.LabResultDetailRequest;
import com.hb.dto.response.LabTestResponse;
import com.hb.dto.response.LabResultResponse;
import com.hb.mapper.LabTestMapper;
import com.hb.mapper.LabResultMapper;
import com.hb.pojo.LabResult;
import com.hb.pojo.LabTest;
import com.hb.pojo.Patient;
import com.hb.pojo.User;
import com.hb.service.LabTestService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api/secure")
public class ApiLabTestResultController {
    
    @Autowired
    private LabTestService testService;

    @Autowired
    private LabResultService testResultService;

    @Autowired
    private LabResultMapper testResultMapper;

    @Autowired
    private UserService userService;
    


    @PostMapping("/lab-results")
    public ResponseEntity<?> create(@RequestBody LabResultCreateRequest req) {
        testResultService.labTestOrder(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/lab-results")
    public ResponseEntity<List<LabResultResponse>> list(@RequestParam Map<String, String> params, 
                                                            Principal principal)
                                                            {
        User u = userService.getUserByUsername(principal.getName());

        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(this.testResultService.getLabResults(params));
    }
    
    @GetMapping("/tests/appointment/{appointmentId}")
    public ResponseEntity<List<LabResultResponse>> list(Principal principal, @PathVariable(value = "appointmentId") Long appointmentId) {
        User u = userService.getUserByUsername(principal.getName());

        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<LabResult> res = this.testResultService.getLabResultsesByAppointmentId(appointmentId);
        return ResponseEntity.ok(res.stream().map(testResultMapper::toResponse).toList());
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
