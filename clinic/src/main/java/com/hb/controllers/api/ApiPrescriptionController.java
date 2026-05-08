/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.PrescriptionCreateRequest;
import com.hb.dto.response.PrescriptionResponse;
import com.hb.mapper.PrescriptionMapper;
import com.hb.pojo.Prescription;
import com.hb.pojo.PrescriptionItem;
import com.hb.service.PrescriptionService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api/secure")
@PropertySource("classpath:configs.properties")
public class ApiPrescriptionController {
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    @Autowired
    private Environment env;
    
    @PostMapping("/prescriptions")
    public ResponseEntity<PrescriptionResponse> create(@RequestBody  PrescriptionCreateRequest req){
        Prescription p =  this.prescriptionService.addPrescription(req);
        PrescriptionResponse res = PrescriptionMapper.INSTANCE.toResponse(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
    
    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionResponse>> list(@RequestParam Map<String,String> params){
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));
        
        List<Prescription> results = this.prescriptionService.getPrescriptions(params);
        List<PrescriptionResponse> res = results.stream().map(PrescriptionMapper.INSTANCE::toResponse).toList();
        
        return ResponseEntity.ok(res);
    }
    
    
//    @GetMapping("prescriptions/{id}")
//    public ResponseEntity<List<PrescriptionResponse>> list(@PathVariable Long id){
//        return null;
//    }
}
