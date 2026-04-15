/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.service.AppointmentService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author HUY
 */
@Controller
@RequestMapping("/admin/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;
    
    @GetMapping("")
    public String list(Model model, @RequestParam Map<String,String> params){
        model.addAttribute("appointments", this.appointmentService.getAppointments(params));
        return "appointment";
    }
}
