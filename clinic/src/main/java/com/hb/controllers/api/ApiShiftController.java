/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.response.ShiftResponse;
import com.hb.service.ShiftService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HUY
 */
@RestController
@RequestMapping("/api")
@PropertySource("classpath:configs.properties")
public class ApiShiftController {
    @Autowired
    private ShiftService shiftService;
    
    @Autowired
    private Environment env;
            
      
    
    
    @GetMapping("/shifts")
    public ResponseEntity<List<ShiftResponse>> list (@RequestParam Map<String,String> params){
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));
        List<ShiftResponse> res = shiftService.getShifts(null);
        return ResponseEntity.ok(res);
    }
    
}
