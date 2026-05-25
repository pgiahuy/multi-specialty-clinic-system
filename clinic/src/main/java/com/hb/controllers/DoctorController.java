/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.pojo.Doctor;
import com.hb.service.DoctorService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
@RequestMapping("/admin")
@PropertySource("classpath:configs.properties")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private Environment env;

    
    @GetMapping("/doctors")
    public String list(Model model, @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        
        params.put("pageSize", String.valueOf(pageSize));
        model.addAttribute("doctors", doctorService.getDoctors(params));
        model.addAttribute("doctor", new Doctor());


        long totalDoctors = doctorService.countDoctors(params);
        int totalPages = (int) Math.ceil((double) totalDoctors / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "doctor";
    }
    

    @PostMapping("/doctors")
    public String create(@RequestParam Map<String, String> params) {

        doctorService.addDoctor(params);
        return "redirect:/admin/doctor";
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
    }
    
}
