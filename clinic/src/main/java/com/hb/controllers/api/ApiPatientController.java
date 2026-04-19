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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @PutMapping(value = "/secure/profile/{id}",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(
            @PathVariable("id") Long id,
            @ModelAttribute PatientCreateRequest prq) {

        patientService.updateProfile(id, prq);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/secure/profile")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<PatientResponse> getProfile(Principal principal) {
        User u = this.userService.getUserByUsername(principal.getName());
        Patient p = u.getPatient();

       
        return ResponseEntity.ok(patientMapper.toResponse(p));
    }

}
