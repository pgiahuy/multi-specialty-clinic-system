/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author HUY
 */

@Controller
public class HomeController {
      
    @Autowired
    private com.hb.service.AppointmentService appointmentService;

    @GetMapping("/") 
    public String index(Model model,
            @org.springframework.web.bind.annotation.RequestParam(name = "month", required = false) String month) {
        model.addAttribute("month", month);
        model.addAttribute("topDoctorByConversionAppointments", appointmentService.getTopDoctorsByConvertedAppointmentCount(10, month));
        model.addAttribute("topDoctorByAppointmentCount", appointmentService.getTopDoctorsByAppointmentCount(10, month));
        return "index";
    }
    
    @GetMapping("/admin/login")
    public String loginView() {
        return "login";
    }
    
   
    
}
