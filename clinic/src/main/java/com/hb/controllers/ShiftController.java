/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.request.form.ShiftForm;
import com.hb.enums.SessionShift;
import com.hb.service.ShiftService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


@Controller
@RequestMapping("/admin")
@PropertySource("classpath:configs.properties")
public class ShiftController {
    
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private Environment env;
    
    @GetMapping("/shifts")
    public String list(Model model ,@RequestParam Map<String,String> params){
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));

        model.addAttribute("shifts", this.shiftService.getShifts(params));
        model.addAttribute("shiftForm", new ShiftForm());
        model.addAttribute("sessions", SessionShift.values());

        long totalShifts = shiftService.countShifts(params);
        int totalPages = (int) Math.ceil((double) totalShifts / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "shift";
    }
    
    @PostMapping("/shifts")
    public String create(@ModelAttribute ShiftForm shiftForm){
        shiftService.saveOrUpdate(shiftForm);
        return "redirect:/admin/shifts";
    }

    @DeleteMapping("/shifts/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            shiftService.deleteShift(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shift not found");
        }
    }
    
}
