/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.response.stats.PatientGenderStatsResponse;
import com.hb.service.StatsService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author HUY
 */
@Controller
@RequestMapping("/admin/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    // @GetMapping("/gender")
    // public String getGenderStats(
    //         @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
    //         @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
    //         Model model) {

    //     if (fromDate == null) {
    //         fromDate = LocalDate.now().minusDays(30);
    //     }
    //     if (toDate == null) {
    //         toDate = LocalDate.now();
    //     }

    //     List<PatientGenderStatsResponse> genderStats = statsService.getPatientGenderStats(fromDate, toDate);

    //     model.addAttribute("genderStats", genderStats);
    //     model.addAttribute("fromDate", fromDate);
    //     model.addAttribute("toDate", toDate);

    //     return "stats/stats";
    // }

    @GetMapping({"/{type}", ""})
    public String getStats(
            @PathVariable(name = "type", required = false) String type,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(value = "dateType", required = false, defaultValue = "CUSTOM") String dateType,
            Model model) {

        if (fromDate == null) {
            fromDate = LocalDate.now().minusDays(30);
        }
        if (toDate == null) {
            toDate = LocalDate.now();
        }

        if (type == null || type.isBlank()) {
            type = "patient";
        }

        model.addAttribute("reportType", type.toUpperCase());
        model.addAttribute("dateType", dateType);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        if ("patient".equalsIgnoreCase(type)) {
            List<PatientGenderStatsResponse> genderStats = statsService.getPatientGenderStats(fromDate, toDate);
            model.addAttribute("genderStats", genderStats);
        } else {
            model.addAttribute("genderStats", java.util.List.of());
        }

        return "stats/stats";
    }
}
