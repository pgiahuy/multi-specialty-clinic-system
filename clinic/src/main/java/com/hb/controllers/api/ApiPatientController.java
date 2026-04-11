/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.service.PatientService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api/patients")
public class ApiPatientController {
    @Autowired
    private PatientService patientService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable("id") Long id,
            @RequestParam Map<String, String> params) {

        patientService.updateProfile(id, params);
        return ResponseEntity.ok("Cập nhật thành công");
    }
}
