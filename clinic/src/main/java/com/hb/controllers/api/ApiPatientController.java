/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.PatientCreateRequest;
import com.hb.dto.response.PatientResponse;
import com.hb.mapper.PatientMapper;
import com.hb.pojo.Patient;
import com.hb.pojo.User;
import com.hb.service.PatientService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiPatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/secure/profiles")
    @Transactional
    public ResponseEntity<?> create(@ModelAttribute PatientCreateRequest req, Principal principal) {
        
        PatientResponse p = patientService.addPatient(req, this.userService.getUserByUsername(principal.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    @GetMapping("/secure/profile/{patientId}")
    public ResponseEntity<?> getProfile(@PathVariable(value = "patientId") Long id, Principal principal) {
        User u = userService.getUserByUsername(principal.getName());
        
        if(!patientService.checkAccess(u, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền truy cập hồ sơ này");
        }

        Patient p = patientService.getPatientById(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thông tin bệnh nhân");
        }

        PatientResponse res = patientMapper.toResponse(p);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/secure/profiles")
    @Transactional
    public ResponseEntity<List<PatientResponse>> getProfiles(Principal principal) {
        User u = this.userService.getUserByUsername(principal.getName());
        List<Patient> res = patientService.getPatientsByUserId(u.getId());
        return ResponseEntity.ok(res.stream().map(patientMapper::toResponse).toList());
    }
    
    
    @PutMapping("/secure/profile/{patientId}")
    @Transactional
    public ResponseEntity<?> updateProfile(Principal principal,
            @PathVariable(value = "patientId") Long patientId,
            @RequestBody PatientCreateRequest prq){
        try {
            
            PatientResponse updatedProfile = patientService.updateProfile(patientId, prq);
            
            
            return ResponseEntity.ok(updatedProfile);
            
        } catch (RuntimeException e) {
           
            return ResponseEntity.badRequest().build(); 
           
        }
    }
    
    @DeleteMapping("/secure/profile/{patientId}")
   
    public ResponseEntity<?> destroy(@PathVariable(value="patientId") Long id, Principal principal) {
        User u = userService.getUserByUsername(principal.getName());
        
        if(!patientService.checkAccess(u, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền truy cập hồ sơ này");
        }
        
        try {
            patientService.deletePatient(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
        
    }

}
