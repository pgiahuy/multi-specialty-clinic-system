/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.request.PatientCreateRequest;
import com.hb.service.PatientService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 * @author HUY
 */


@PropertySource("classpath:configs.properties")
@Controller
@RequestMapping("/admin/patients")
public class PatientController {
    
    @Autowired
    private PatientService patientService;

    @Autowired
    private Environment env;

    @GetMapping("")
    public String list(Model model, @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));
        model.addAttribute("patients", patientService.getPatients(params));
        model.addAttribute("patient", new PatientCreateRequest());

        long totalPatients = patientService.countPatients(params);
        int totalPages = (int) Math.ceil((double) totalPatients / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "patient";
    }
    
    


}
