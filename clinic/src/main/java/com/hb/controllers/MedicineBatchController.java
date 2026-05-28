/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.request.form.MedicineBatchForm;
import com.hb.pojo.Medicine;
import com.hb.service.MedicineBatchService;
import com.hb.service.MedicineService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class MedicineBatchController {
    @Autowired
    private MedicineBatchService medicineBatchService;
    @Autowired
    private Environment env;
    
    @GetMapping("/medicine-batchs")
    public String list(Model model, @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));
        model.addAttribute("medicineBatchs", this.medicineBatchService.getMedicineBatchs(params));
        model.addAttribute("kw", params.get("kw"));
        MedicineBatchForm medicineBatchForm = new MedicineBatchForm();
        medicineBatchForm.setMedicine(new Medicine());
        model.addAttribute("medicineBatchForm", medicineBatchForm);

        long totalMedicineBtachs = medicineBatchService.countMedicineBatchs(params);
        int totalPages = (int) Math.ceil((double) totalMedicineBtachs / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "medicine-batch";
    }

    @PostMapping("/medicine-batchs")
    public String create(@ModelAttribute MedicineBatchForm medicineBatchForm) {
        medicineBatchService.addOrUpdateMedicineBatch(medicineBatchForm);
        return "redirect:/admin/medicine-batchs";
    }

    @DeleteMapping("/medicine-batchs/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            medicineBatchService.deleteMedicineBatch(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }
}
