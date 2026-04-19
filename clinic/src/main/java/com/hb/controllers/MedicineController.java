/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.service.MedicineService;
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
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author HUY
 */
@Controller
@RequestMapping("/admin/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping("")
    public String list(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("medicines", this.medicineService.getMedicines(params));
        return "medicine";
    }

    @PostMapping("")
    public String create(@RequestParam Map<String, String> params,
            @RequestParam("image") MultipartFile avatar) {
        medicineService.addMedicine(params, avatar);
        return "redirect:/admin/medicines";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return "redirect:/admin/medicines";
    }
}
