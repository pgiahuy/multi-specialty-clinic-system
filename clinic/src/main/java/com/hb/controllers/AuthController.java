/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.service.AuthService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DELL
 */
@Controller
public class AuthController {
    @Autowired
    private AuthService authService;
    
    
    @GetMapping("/register")
    public String registerView() {
        return "register";
    }
    
    @PostMapping("register")
    public String create(@RequestParam Map<String, String> params, 
            @RequestParam(value = "avatar") MultipartFile avatar, Model model){
        try {
            authService.registerPatient(params, avatar);
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    
    @GetMapping("/login")
    public String loginView() {
        return "login";
    }
}
