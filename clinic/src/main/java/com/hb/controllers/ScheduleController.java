/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.service.ScheduleService;
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
@RequestMapping("/admin/schedules")
public class ScheduleController {
    
    @Autowired
    private ScheduleService scheduleService;
    

    @GetMapping("")
    public String list(Model model ,@RequestParam Map<String,String> params){
        model.addAttribute("schedules", this.scheduleService.getSchedules(params));
        return "schedule";
    }
    
    @PostMapping("")
    public String create(@RequestParam Map<String, String> params){
        scheduleService.addSchedule(params);
        return "redirect:/admin/schedules";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return "redirect:/admin/schedules";
    }
    
}