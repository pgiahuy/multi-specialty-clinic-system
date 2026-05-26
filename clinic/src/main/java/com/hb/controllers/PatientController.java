/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.request.PatientCreateRequest;
import com.hb.dto.request.form.PatientForm;
import com.hb.service.PatientService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 * @author HUY
 */


@PropertySource("classpath:configs.properties")
@Controller
@RequestMapping("/admin")
public class PatientController {
    
    @Autowired
    private PatientService patientService;

    @Autowired
    private Environment env;

    @GetMapping("/patients")
    public String list(Model model, @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));
        model.addAttribute("patients", patientService.getPatients(params));
        model.addAttribute("patientForm", new PatientForm());

        long totalPatients = patientService.countPatients(params);
        int totalPages = (int) Math.ceil((double) totalPatients / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "patient";
    }
    
    @PostMapping("/patients")
    public String create(@ModelAttribute PatientForm patientForm){
        patientService.saveOrUpdate(patientForm);
        return "redirect:/admin/patients";
    }

    @DeleteMapping("/patients/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
    
    


}
