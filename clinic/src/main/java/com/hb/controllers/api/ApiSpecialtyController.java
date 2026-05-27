/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.response.SpecialtyResponse;
import com.hb.mapper.SpecialtyMapper;
import com.hb.pojo.Specialty;
import com.hb.service.SpecialtyService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiSpecialtyController {
    @Autowired
    private SpecialtyService specialtySer;
    
    @Autowired
    private SpecialtyMapper mapper;
    
    @GetMapping("/specialties")
    public ResponseEntity<List<SpecialtyResponse>> list(@RequestParam Map<String, String> params) {
        List<Specialty> list = this.specialtySer.getSpecialties(params);
        return ResponseEntity.ok(list.stream().map(mapper :: toResponse).toList());
        
    }
}
