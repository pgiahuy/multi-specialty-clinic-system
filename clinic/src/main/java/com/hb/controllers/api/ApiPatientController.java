/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.request.PatientCreateRequest;
import com.hb.dto.response.PatientResponse;
import com.hb.mapper.PatientMapper;
import com.hb.pojo.User;
import com.hb.service.PatientService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api")
public class ApiPatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<PatientResponse> create(@RequestBody PatientCreateRequest req, Principal principal) {
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
    public ResponseEntity<List<PatientResponse>> getProfiles(Principal principal) {

        if (principal == null) {
            System.out.println("==========================");
            System.out.println("==========401===============");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User u = userService.getUserByUsername(principal.getName());

        List<PatientResponse> result = u.getPatientCollection()
                .stream()
                .map(patientMapper::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

}
