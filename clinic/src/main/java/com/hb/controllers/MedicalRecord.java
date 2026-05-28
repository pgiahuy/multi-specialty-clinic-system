/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.request.MedicalRecordCreateRequest;
import com.hb.service.MedicalRecordService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author HUY
 */
@PropertySource("classpath:configs.properties")
@Controller
@RequestMapping("/admin/medical-records")
public class MedicalRecord {
    
    @Autowired
    private MedicalRecordService medicalRecordService;
    @Autowired
    private Environment env;
    @GetMapping("")
    public String list(Model model, @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));
        model.addAttribute("medical_records", medicalRecordService.getMedicalRecords(params));

        long totalMedicalRecords = medicalRecordService.countMedicalRecords(params);
        int totalPages = (int) Math.ceil((double) totalMedicalRecords / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "medical-record";
    }

    @PostMapping("")
    public String create(@RequestBody MedicalRecordCreateRequest req) {
        medicalRecordService.addOrUpdateMedicalRecord(req);
        return "redirect:/admin/medical-records";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return "redirect:/admin/medical-records";
    }
    
}
    

