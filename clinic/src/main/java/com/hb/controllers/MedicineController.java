/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.request.form.MedicineForm;
import com.hb.service.MedicineService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
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

/**
 *
 * @author HUY
 */

@PropertySource("classpath:configs.properties")
@Controller
@RequestMapping("/admin")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;
    @Autowired
    private Environment env;
    
    @GetMapping("/medicines")
    public String list(Model model, @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));
        model.addAttribute("medicines", this.medicineService.getMedicines(params));
        model.addAttribute("medicineForm", new MedicineForm());

        long totalMedicines = medicineService.countMedicines(params);
        int totalPages = (int) Math.ceil((double) totalMedicines / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "medicine";
    }

    @PostMapping("/medicines")
    public String create(@ModelAttribute("medicineForm") MedicineForm medicineForm) {
        medicineService.addOrUpdateMedicine(medicineForm);
        return "redirect:/admin/medicines";
    }

    @DeleteMapping("/medicines/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            medicineService.deleteMedicine(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine not found");
        }
    }

    @GetMapping("/medicines/search")
    @ResponseBody
    public List<Map<String, Object>> searchMedicines(@RequestParam(value = "kw", required = false) String kw) {
        Map<String, String> params = new HashMap<>();
        if (kw != null && !kw.trim().isEmpty()) {
            params.put("kw", kw.trim());
        }

        return this.medicineService.getMedicines(params).stream()
                .map(m -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", m.getId());
                    item.put("code", m.getCode());
                    item.put("name", m.getName());
                    return item;
                })
                .collect(Collectors.toList());
    }
}
