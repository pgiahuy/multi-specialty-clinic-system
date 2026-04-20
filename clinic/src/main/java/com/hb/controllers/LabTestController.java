/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.service.LabTestService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author DELL
 */

@PropertySource("classpath:configs.properties")
@Controller
@RequestMapping("/admin")
public class LabTestController {

    @Autowired
    private LabTestService labTestService;
    @Autowired
    private Environment env;
    @GetMapping("/lab-tests")
    public String list(Model model, @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));
        model.addAttribute("labTests", labTestService.getLabTests(params));

        long totalLabTests = labTestService.countLabTests(params);
        int totalPages = (int) Math.ceil((double) totalLabTests / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "labtest";
    }

    @PostMapping("/lab-tests")
    public String create(@RequestParam Map<String, String> params) {
        labTestService.addOrUpdateLabTest(params);
        return "redirect:/admin/lab-tests";
    }

    @DeleteMapping("/lab-tests/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Long id) {
        labTestService.deleteLabTest(id);
    }
}
