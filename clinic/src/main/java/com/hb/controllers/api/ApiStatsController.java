/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers.api;

import com.hb.dto.response.stats.PatientGenderStatsResponse;
import com.hb.service.StatsService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/admin/stats")
public class ApiStatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/patient/{type}")
    public ResponseEntity<List<?>> getGenderStats(
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @PathVariable("type") String type) {

        if (fromDate == null) {
            fromDate = LocalDate.now().minusDays(30);
        }
        if (toDate == null) {
            toDate = LocalDate.now();
        }
        
        List<?> res = null;
        
        if (type != null) {
            switch (type) {
                case "gender":
                    res = statsService.getPatientGenderStats(fromDate, toDate);
                    break;
                case "age":
                    res = statsService.getPatientAgeGroupStats(fromDate, toDate);
                    break;
                    
                case "specialty":
                    res = statsService.getPatientSpecialtyStats(fromDate, toDate);
                    break;

                default:
                    throw new AssertionError();
            }
        }


        return ResponseEntity.ok(res);
    }
    
    

}
