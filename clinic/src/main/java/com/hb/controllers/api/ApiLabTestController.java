/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.LabTestResultRequest;
import com.hb.dto.response.LabTestResultResponse;
import com.hb.mapper.LabTestResultMapper;
import com.hb.pojo.LabResults;
import com.hb.service.LabTestResultService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api/secure")
public class ApiLabTestController {
    @Autowired
    private LabTestResultService testResultService;
    
    @Autowired
    private LabTestResultMapper testResultMapper;
    
    @PostMapping("/test")
    public ResponseEntity<List<LabTestResultResponse>> create(@RequestBody List<LabTestResultRequest> reqs, Principal principal) {
        List<LabResults> res = testResultService.addMutipleTest(reqs);
        List<LabTestResultResponse> responseList = res.stream()
                                                  .map(testResultMapper::toResponse)
                                                  .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
    }
}
