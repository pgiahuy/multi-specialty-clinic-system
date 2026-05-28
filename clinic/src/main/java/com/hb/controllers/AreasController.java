/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.request.form.AreaForm;
import com.hb.service.AreasService;
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
 * @author DELL
 */



@PropertySource("classpath:configs.properties")
@Controller
@RequestMapping("/admin")
public class AreasController {
    @Autowired
    private AreasService areaService;
    
    @Autowired
    private Environment env;
    
    @GetMapping("/areas")
    public String list(Model model, @RequestParam Map<String, String> params) {
        System.out.print("================");
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));
        model.addAttribute("areas", areaService.getAreas(params));
        model.addAttribute("areaForm", new AreaForm());

        long totalAreas = areaService.countAreas(params);
        int totalPages = (int) Math.ceil((double) totalAreas / pageSize);
        
        

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "area";
    }
    

    @PostMapping("/areas")
    public String create(@ModelAttribute AreaForm areaForm) {

        areaService.saveOrUpdate(areaForm);
        return "redirect:/admin/areas";
    }

    @DeleteMapping("/areas/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            areaService.deleteAreas(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Area not found");
        }
    }
}
