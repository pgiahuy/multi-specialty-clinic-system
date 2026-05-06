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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    

    
    @PostMapping(value = "/secure/profiles")
    @Transactional
    public ResponseEntity<PatientResponse> create(@ModelAttribute PatientCreateRequest req ,Principal principal){
        User u = this.userService.getUserByUsername(principal.getName());
        PatientResponse p = patientService.addPatient(req, u);
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }
    

//    @PutMapping(value = "/secure/profile/{id}",
//        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> updateProfile(
//            @PathVariable("id") Long id,
//            @ModelAttribute PatientCreateRequest prq) {
//
//        patientService.updateProfile(id, prq);
//        return ResponseEntity.ok().build();
//    }
 

    @GetMapping("/secure/profiles")
    @Transactional
    public ResponseEntity<List<PatientResponse>> getProfile(Principal principal) {
        User u = this.userService.getUserByUsername(principal.getName());
        System.out.printf("=============%s==============", principal.getName());
        List<Patient> patients = (List<Patient>) u.getPatientCollection();
        patients.forEach(s-> System.out.println(s.getFullName()));
        return ResponseEntity.ok(patients.stream().map(patientMapper::toResponse).toList());
    }

}
