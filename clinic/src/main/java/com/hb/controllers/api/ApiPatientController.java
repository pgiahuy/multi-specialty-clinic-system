/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.pojo.Patient;
import com.hb.pojo.User;
import com.hb.service.PatientService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api/patients")
public class ApiPatientController {
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable("id") Long id,
            @RequestParam Map<String, String> params,
            @RequestParam(value = "avatar") MultipartFile avatar) {

        patientService.updateProfile(id, params,avatar);
        return ResponseEntity.ok("Cập nhật thành công");
    }
    
    @RequestMapping("/secure/profile")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Patient> getProfile(Principal principal) {
        User u = this.userService.getUserByUsername(principal.getName());
        return new ResponseEntity<>(u.getPatient(), HttpStatus.OK);
    }
}
