/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.request.UserCreateRequest;
import com.hb.pojo.User;
import com.hb.service.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author HUY
 */
@Controller
@RequestMapping("/admin")
@PropertySource("classpath:configs.properties")
public class UserController {

    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String list(Model model, @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));

        model.addAttribute("users", userService.getUsers(params));
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserCreateRequest());
        }

        long totalUsers = userService.countUsers(params);
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "user";
    }

    @PostMapping("/users")
    public String create(@ModelAttribute("user") UserCreateRequest urq, RedirectAttributes redirectAttributes) {
        try {
            userService.saveOrUpdateUser(urq);
            redirectAttributes.addFlashAttribute("successMsg", "Thao tác dữ liệu thành công!");
        } catch (Exception e) {
            urq.setAvatar(null);
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            redirectAttributes.addFlashAttribute("user", urq);
            redirectAttributes.addFlashAttribute("openForm", true);
        }

        return "redirect:/admin/users";
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
