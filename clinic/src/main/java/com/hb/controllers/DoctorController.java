/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.request.form.DoctorForm;

import com.hb.service.DoctorService;
import com.hb.service.SpecialtyService;
import com.hb.service.UserService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private UserService userService;
    
    @Autowired
    private SpecialtyService specialtyService;
    
    @Autowired
    private Environment env;

    
    @GetMapping("/doctors")
    public String list(Model model, @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        
        params.put("pageSize", String.valueOf(pageSize));
        model.addAttribute("doctors", doctorService.getDoctors(params));
        model.addAttribute("doctorForm", new DoctorForm());


        long totalDoctors = doctorService.countDoctors(params);
        int totalPages = (int) Math.ceil((double) totalDoctors / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "doctor";
    }
    

    @PostMapping("/doctors")
    public String create(@ModelAttribute DoctorForm doctorForm) {
        doctorService.saveOrUpdate(doctorForm);
        return "redirect:/admin/doctors";
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

    @GetMapping("/doctors/search-users")
    @ResponseBody
    public List<Map<String, Object>> searchUsers(@RequestParam(value = "kw", required = false) String kw) {
        Map<String, String> params = new HashMap<>();
        if (kw != null && !kw.trim().isEmpty()) {
            params.put("kw", kw.trim());
        }

        return this.userService.getUsers(params).stream()
                .map(u -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", u.getId());
                    item.put("username", u.getUsername());
                    return item;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/doctors/search-specialties")
    @ResponseBody
    public List<Map<String, Object>> searchSpecialties(@RequestParam(value = "kw", required = false) String kw) {
        Map<String, String> params = new HashMap<>();
        if (kw != null && !kw.trim().isEmpty()) {
            params.put("kw", kw.trim());
        }

        return this.specialtyService.getSpecialties(params).stream()
                .map(s -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", s.getId());
                    item.put("name", s.getName());
                    return item;
                })
                .collect(Collectors.toList());
    }
    
}
