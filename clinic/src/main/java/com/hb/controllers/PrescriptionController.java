/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.service.PrescriptionService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author HUY
 */



@PropertySource("classpath:configs.properties")
@Controller
@RequestMapping("/admin/prescriptions")
public class PrescriptionController {
    @Autowired
    private Environment env;
    @Autowired
    private PrescriptionService prescriptionService;
    
    @GetMapping("")
    public String list(Model model, @RequestParam Map<String,String> params){
        
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));

        
        model.addAttribute("prescriptions", this.prescriptionService.getPrescriptions(params));
        
        long totalRooms = prescriptionService.countPrescription(params);
        int totalPages = (int) Math.ceil((double) totalRooms / pageSize);

        
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "prescription";
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            prescriptionService.deletePrescription(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đơn thuốc!");
        }
    }
}
