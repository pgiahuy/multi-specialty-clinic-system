/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.service.SpecialtyService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author HUY
 */

@Controller
@RequestMapping("/admin/specialties")
public class SpecialtyController {
    @Autowired
    private SpecialtyService specialtyService;
    
    @GetMapping("")
    public String list(Model model, @RequestParam Map<String,String> params){
        model.addAttribute("specialties" , this.specialtyService.getSpecialties(params));
        return "specialty";
    }
    
    @PostMapping("")
    public String create(@RequestParam Map<String, String> params) {
        specialtyService.addSpecialtie(params);
        return "redirect:/admin/specialties";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            specialtyService.deleteDoctor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy chuyên khoa");
        }
    }
}
