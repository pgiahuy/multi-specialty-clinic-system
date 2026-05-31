/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.MedicalRecordCreateRequest;
import com.hb.dto.response.MedicalRecordResponse;
import com.hb.mapper.MedicalRecordMapper;
import com.hb.pojo.MedicalRecord;
import com.hb.pojo.User;
import com.hb.service.MedicalRecordService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
@RequestMapping("api/secure")
@PropertySource("classpath:configs.properties")
@CrossOrigin
public class ApiMedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @Autowired
    private MedicalRecordMapper recordMapper;

    @PostMapping("/medical-records")
    public ResponseEntity<MedicalRecordResponse> create(@RequestBody MedicalRecordCreateRequest req) {
        MedicalRecord record = medicalRecordService.addOrUpdateMedicalRecord(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(recordMapper.toResponse(record));
    }

    @PutMapping("medical-records/{id}")
    public ResponseEntity<MedicalRecordResponse> update(@PathVariable(value = ("id")) Long id
                                                        ,@RequestBody MedicalRecordCreateRequest req) {
        req.setId(id);
        MedicalRecord record = medicalRecordService.addOrUpdateMedicalRecord(req);
        return ResponseEntity.ok(recordMapper.toResponse(record));
    }
    
    

    @GetMapping("/medical-records")
    public ResponseEntity<List<MedicalRecordResponse>> list(@RequestParam Map<String, String> params, Principal principal) {
        User u = userService.getUserByUsername(principal.getName());
        if (u != null) {
            params.put("currentUserId", String.valueOf(u.getId()));
            params.put("currentUserRole", u.getRole().toString());
        }
        int pageSize = this.env.getProperty("admin.page_size", Integer.class, 10);
        params.put("pageSize", String.valueOf(pageSize));

        List<MedicalRecord> res = medicalRecordService.getMedicalRecords(params);
        return ResponseEntity.ok(res.stream().map(MedicalRecordMapper.INSTANCE::toResponse).toList());
    }
    
    @GetMapping("/medical-records/{id}")
    public ResponseEntity<MedicalRecordResponse> getById(@PathVariable("id") Long id,Principal principal) {
        String userName = principal.getName();
        User u = userService.getUserByUsername(userName);
        boolean isAccess = medicalRecordService.checkAccessControll(u, id);
        
        if (!isAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        MedicalRecord res = medicalRecordService.getMedicalRecordById(id);

        return ResponseEntity.ok(MedicalRecordMapper.INSTANCE.toResponse(res));
    }

    @GetMapping("/medical-records/patient/{patient-id}")
    public ResponseEntity<List<MedicalRecordResponse>> getByPatientId(@PathVariable("patient-id") Long patienId, Principal principal) {
        String userName = principal.getName();
        boolean isAccess = medicalRecordService.checkAccessControll(userName, patienId);
        if (!isAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<MedicalRecord> res = medicalRecordService.getMedicalRecordsByPatientId(patienId);

        return ResponseEntity.ok(res.stream().map(MedicalRecordMapper.INSTANCE::toResponse).toList());
    }

    @GetMapping("medical-records/appointment/{appointmentId}")
    public ResponseEntity<MedicalRecordResponse> getByAppointmentId(@PathVariable(value = "appointmentId") Long appointmentId,
            Principal principal) {
        MedicalRecord res = medicalRecordService.getMedicalRecordByAppointmentId(appointmentId);
        return ResponseEntity.ok(MedicalRecordMapper.INSTANCE.toResponse(res));
    }

    
}
