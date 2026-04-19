/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.service.ShiftService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin/shifts")
public class ShiftController {
    
    @Autowired
    private ShiftService shiftService;
    
    @GetMapping("")
    public String list(Model model ,@RequestParam Map<String,String> params){
        model.addAttribute("shifts", this.shiftService.getShifts(params));
        return "shift";
    }
    
    @PostMapping("")
    public String create(@RequestParam Map<String, String> params){
        shiftService.addShift(params);
        return "redirect:/admin/shift";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return "redirect:/admin/shift";
    }
    
}
