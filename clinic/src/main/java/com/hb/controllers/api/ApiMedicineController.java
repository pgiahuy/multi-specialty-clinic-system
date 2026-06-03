/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.response.MedicineBatchResponse;
import com.hb.dto.response.MedicineResponse;
import com.hb.dto.response.StorekeeperAlertResponse;
import com.hb.enums.UserRole;
import com.hb.pojo.User;
import com.hb.service.MedicineBatchService;
import com.hb.service.MedicineService;
import com.hb.service.UserService;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @PreAuthorize("hasAnyRole('DOCTOR','STAFF','STOREKEEPER')")
    public ResponseEntity<List<MedicineResponse>> list(@RequestParam Map<String, String> params, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserRole role = u.getRole();
        if (role == null || (!UserRole.ROLE_DOCTOR.equals(role)
                && !UserRole.ROLE_STOREKEEPER.equals(role)
                && !UserRole.ROLE_STAFF.equals(role))) {
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<MedicineResponse> meds = medicineService.getMedicinesWithStock(params);

        return ResponseEntity.ok(meds);
    }

    @GetMapping("/medicines/count")
    @PreAuthorize("hasAnyRole('DOCTOR','STAFF','STOREKEEPER')")
    public ResponseEntity<Long> countMedicines(@RequestParam Map<String, String> params, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserRole role = u.getRole();
        if (role == null || (!UserRole.ROLE_DOCTOR.equals(role)
                && !UserRole.ROLE_STOREKEEPER.equals(role)
                && !UserRole.ROLE_STAFF.equals(role))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        long count = medicineService.countMedicines(params);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/storekeeper/alerts")
    @PreAuthorize("hasAnyRole('DOCTOR','STAFF','STOREKEEPER')")
    public ResponseEntity<StorekeeperAlertResponse> getStorekeeperAlerts(@RequestParam Map<String, String> params, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserRole role = u.getRole();
        if (role == null || (!UserRole.ROLE_DOCTOR.equals(role)
                && !UserRole.ROLE_STOREKEEPER.equals(role)
                && !UserRole.ROLE_STAFF.equals(role))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        int threshold = 10;
        if (params != null && params.containsKey("lowStockThreshold") && params.get("lowStockThreshold") != null) {
            try {
                threshold = Integer.parseInt(params.get("lowStockThreshold"));
            } catch (NumberFormatException e) {
                threshold = 10;
            }
        }

        int expiryDays = 30;
        if (params != null && params.containsKey("expiryDays") && params.get("expiryDays") != null) {
            try {
                expiryDays = Integer.parseInt(params.get("expiryDays"));
            } catch (NumberFormatException e) {
                expiryDays = 30;
            }
        }

        Map<String, String> lowStockParams = new HashMap<>();
        if (params != null) {
            lowStockParams.putAll(params);
        }
        lowStockParams.put("lowStockThreshold", String.valueOf(threshold));

        if (params != null) {
            if (params.get("lowStockPage") != null) {
                lowStockParams.put("page", params.get("lowStockPage"));
            }
            if (params.get("lowStockPageSize") != null) {
                lowStockParams.put("pageSize", params.get("lowStockPageSize"));
            }
        }

        LocalDate fromExpiry = LocalDate.now().minusYears(100);
        LocalDate toExpiry = LocalDate.now().plusDays(expiryDays);
        Map<String, String> expiryParams = new HashMap<>();
        expiryParams.put("fromExpiry", fromExpiry.toString());
        expiryParams.put("toExpiry", toExpiry.toString());

        if (params != null) {
            if (params.get("expiryPage") != null) {
                expiryParams.put("page", params.get("expiryPage"));
            }
            if (params.get("expiryPageSize") != null) {
                expiryParams.put("pageSize", params.get("expiryPageSize"));
            }
            if (params.get("page") != null && !expiryParams.containsKey("page")) {
                expiryParams.put("page", params.get("page"));
            }
            if (params.get("pageSize") != null && !expiryParams.containsKey("pageSize")) {
                expiryParams.put("pageSize", params.get("pageSize"));
            }
        }

        List<MedicineResponse> lowStock = medicineService.getLowStockMedicines(lowStockParams);
        List<MedicineBatchResponse> expiringBatches = medicineBatchService.getExpiringBatches(expiryParams)
                .stream()
                .map(MedicineBatchResponse::fromEntity)
                .collect(Collectors.toList());

        StorekeeperAlertResponse alertResponse = new StorekeeperAlertResponse(lowStock, expiringBatches);
        return ResponseEntity.ok(alertResponse);
    }

    @DeleteMapping("/medicine-batchs/{id}")
    @PreAuthorize("hasRole('STOREKEEPER')")
    public ResponseEntity<?> deleteMedicineBatch(@PathVariable("id") Long id, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserRole role = u.getRole();
        if (role == null || (!UserRole.ROLE_STOREKEEPER.equals(role))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            medicineBatchService.destroyMedicineBatch(id, u.getUsername());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/medicine-batchs")
    @PreAuthorize("hasAnyRole('DOCTOR','STAFF','STOREKEEPER')")
    public ResponseEntity<List<MedicineBatchResponse>> listMedicineBatchs(@RequestParam Map<String, String> params, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserRole role = u.getRole();
        if (role == null || (!UserRole.ROLE_DOCTOR.equals(role)
                && !UserRole.ROLE_STOREKEEPER.equals(role)
                && !UserRole.ROLE_STAFF.equals(role))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<MedicineBatchResponse> batchs = medicineBatchService.getMedicineBatchs(params)
                .stream()
                .map(MedicineBatchResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(batchs);
    }

    @GetMapping("/medicine-batchs/count")
    @PreAuthorize("hasAnyRole('DOCTOR','STAFF','STOREKEEPER')")
    public ResponseEntity<Long> countMedicineBatchs(@RequestParam Map<String, String> params, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserRole role = u.getRole();
        if (role == null || (!UserRole.ROLE_DOCTOR.equals(role)
                && !UserRole.ROLE_STOREKEEPER.equals(role)
                && !UserRole.ROLE_STAFF.equals(role))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        long count = medicineBatchService.countMedicineBatchs(params);
        return ResponseEntity.ok(count);
    }
}
