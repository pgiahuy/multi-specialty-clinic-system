/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.service.UserService;
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
 * @author HUY
 */
@Controller
@RequestMapping("/admin/users")
public class UserComtroller {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String createView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("users", userService.getUsers(params));
        return "users";
    }

    @PostMapping("")
    public String create(@RequestParam Map<String, String> params,
            @RequestParam("avatar") MultipartFile avatar) {

        userService.addUser(params, avatar);

        return "redirect:/admin/users";
    }
}
