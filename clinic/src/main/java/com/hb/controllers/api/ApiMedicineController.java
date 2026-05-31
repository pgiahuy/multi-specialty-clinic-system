/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.response.MedicineResponse;
import com.hb.pojo.Medicine;
import com.hb.pojo.User;
import com.hb.service.MedicineBatchService;
import com.hb.service.MedicineService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
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
@RequestMapping("api/secure")
@PropertySource("classpath:configs.properties")
public class ApiMedicineController {
    @Autowired
    private Environment env;
    
    @Autowired
    private MedicineService medicineService;

    @Autowired
    private MedicineBatchService medicineBatchService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/medicines")
    public ResponseEntity<List<MedicineResponse>> list(@RequestParam Map<String,String> params, Principal principal ){
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String role = u.getRole();
        if (role == null || !"ROLE_DOCTOR".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Medicine> meds = medicineService.getMedicines(params);
        Map<Long, Integer> stockByMedicineId = new HashMap<>();
        List<?> medicineBatches = medicineBatchService.getMedicineBatchs(new HashMap<>());
        if (medicineBatches != null) {
            medicineBatches.forEach(item -> {
                if (item instanceof com.hb.pojo.MedicineBatch batch && batch.getMedicineId() != null) {
                    Long medicineId = batch.getMedicineId().getId();
                    stockByMedicineId.merge(medicineId, batch.getQuantity(), Integer::sum);
                }
            });
        }

        List<MedicineResponse> resp = meds.stream().map(m -> {
            return new MedicineResponse(
                    m.getId(),
                    m.getCode(),
                    m.getName(),
                    m.getPrice(),
                    m.getUnit(),
                    stockByMedicineId.getOrDefault(m.getId(), 0)
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(resp);
    }
}
