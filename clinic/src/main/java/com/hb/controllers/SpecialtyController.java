/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.request.form.SpecialtyForm;
import com.hb.pojo.Doctor;
import com.hb.service.DoctorService;
import com.hb.service.SpecialtyService;
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
@RequestMapping("/admin/specialties")
@PropertySource("classpath:configs.properties")
public class SpecialtyController {
    @Autowired
    private SpecialtyService specialtyService;
    
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private Environment env;
    
    @GetMapping("")
    public String list(Model model, @RequestParam Map<String,String> params){
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));


        model.addAttribute("specialties" , this.specialtyService.getSpecialties(params));
        model.addAttribute("specialtyForm" , new SpecialtyForm());
        long totalSpecialties = specialtyService.countSpecialties(params);
        int totalPages = (int) Math.ceil((double) totalSpecialties / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "specialty";
    }
    
    @PostMapping("")
    public String create(@ModelAttribute SpecialtyForm specialtyForm) {
        specialtyService.saveOrUpdate(specialtyForm);
        return "redirect:/admin/specialties";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        specialtyService.deleteSpecialty(id);
        return ResponseEntity.noContent().build();
        
    }

    @GetMapping("/search-doctors")
    @ResponseBody
    public List<Map<String, Object>> searchDoctors(@RequestParam(value = "kw", required = false) String kw) {
        Map<String, String> params = new HashMap<>();
        if (kw != null && !kw.trim().isEmpty()) {
            params.put("doctorName", kw.trim());
        }

        return this.doctorService.getDoctors(params).stream()
                .map(d -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", d.getId());
                    item.put("fullName", d.getFullName());
                    return item;
                })
                .collect(Collectors.toList());
    }
}
