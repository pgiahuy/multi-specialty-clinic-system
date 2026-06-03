/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.response.stats.PatientGenderStatsResponse;
import com.hb.dto.response.stats.MedicineInventoryStatsResponse;
import com.hb.dto.response.stats.RevenueBySpecialtyStatsResponse;
import com.hb.dto.response.stats.RevenueByTypeStatsResponse;
import com.hb.dto.response.stats.RevenueStatsResponse;
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
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.hb.dto.response.stats.PatientAgeGroupStatsResponse;
import com.hb.dto.response.stats.PatientSpecialtyStatsResponse;
import com.hb.dto.response.stats.ServiceUsageStatsResponse;
import com.hb.dto.response.stats.RevenueStatsResponse;
import com.hb.dto.response.stats.RevenueBySpecialtyStatsResponse;
import com.hb.dto.response.stats.RevenueByTypeStatsResponse;

/**
 *
 * @author HUY
 */
@Controller
@RequestMapping("/admin/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;


    @GetMapping({"/{type}", ""})
    public String getStats(
            @PathVariable(name = "type", required = false) String type,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(value = "dateType", required = false, defaultValue = "CUSTOM") String dateType,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            Model model) {

        int currentYear = LocalDate.now().getYear();
        int selectedYear = (year != null) ? year : currentYear;
        int selectedMonth = (month != null) ? month : 0;

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
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("selectedMonth", selectedMonth);

        if ("patient".equalsIgnoreCase(type)) {
            List<PatientGenderStatsResponse> genderStats = statsService.getPatientGenderStats(fromDate, toDate);
            model.addAttribute("genderStats", genderStats);
            model.addAttribute("ageStats", statsService.getPatientAgeGroupStats(fromDate, toDate));
            
        } else if ("revenue".equalsIgnoreCase(type)) {
            List<RevenueStatsResponse> revenueStats = statsService.getRevenueStatsByYear(selectedYear);
            List<RevenueBySpecialtyStatsResponse> revenueBySpecialty = statsService.statsRevenueBySpecialty(selectedYear, selectedMonth);
            List<RevenueByTypeStatsResponse> revenueByType = statsService.statsRevenueByType(selectedYear, selectedMonth);

            model.addAttribute("revenueStats", revenueStats);
            model.addAttribute("revenueBySpecialty", revenueBySpecialty);
            model.addAttribute("revenueByType", revenueByType);
        } else if ("disease".equalsIgnoreCase(type)){
            model.addAttribute("specialtyStats", statsService.getPatientSpecialtyStats(fromDate, toDate));
        } else if ("service".equalsIgnoreCase(type)) {
            model.addAttribute("serviceStats", statsService.statsServiceUsage(fromDate, toDate));
        } else if ("medicine".equalsIgnoreCase(type)) {
            model.addAttribute("medicineStats", statsService.getMedicineInventoryStats(fromDate, toDate));
        }
            else {
            model.addAttribute("genderStats", java.util.List.of());
        }

        return "stats/stats";
    }

    @GetMapping("/{type}/export")
        public void exportStats(
            @PathVariable(name = "type", required = true) String type,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(value = "yearValue", required = false) Integer yearValue,
            @RequestParam(value = "month", required = false) Integer month,
            HttpServletResponse response) throws IOException {

        if (fromDate == null) {
            fromDate = LocalDate.now().minusDays(30);
        }
        if (toDate == null) {
            toDate = LocalDate.now();
        }

        response.setContentType("text/csv; charset=UTF-8");
        String filename = "stats-export.csv";
        if ("medicine".equalsIgnoreCase(type)) {
            filename = "medicine-stats.csv";
        } else if ("service".equalsIgnoreCase(type)) {
            filename = "service-stats.csv";
        } else if ("patient".equalsIgnoreCase(type)) {
            filename = "patient-stats.csv";
        } else if ("disease".equalsIgnoreCase(type)) {
            filename = "disease-stats.csv";
        } else if ("revenue".equalsIgnoreCase(type)) {
            filename = "revenue-stats.csv";
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        try (PrintWriter pw = response.getWriter()) {
            if ("medicine".equalsIgnoreCase(type)) {
                List<MedicineInventoryStatsResponse> data = statsService.getMedicineInventoryStats(fromDate, toDate);
                pw.println("Medicine,Imported,Exported");
                if (data != null) {
                    for (MedicineInventoryStatsResponse r : data) {
                        String name = r.getMedicineName() != null ? r.getMedicineName().replaceAll(",", " ") : "";
                        pw.printf("%s,%d,%d\n", name, r.getImportedQty() == null ? 0L : r.getImportedQty(), r.getExportedQty() == null ? 0L : r.getExportedQty());
                    }
                }

            } else if ("service".equalsIgnoreCase(type)) {
                pw.println("Service,UsageCount");
                List<ServiceUsageStatsResponse> data = statsService.statsServiceUsage(fromDate, toDate);
                if (data != null) {
                    for (ServiceUsageStatsResponse r : data) {
                        String name = r.getServiceName() != null ? r.getServiceName().replaceAll(",", " ") : "";
                        pw.printf("%s,%d\n", name, r.getUsageCount() == null ? 0L : r.getUsageCount());
                    }
                }

            } else if ("patient".equalsIgnoreCase(type)) {
                pw.println("--Gender Distribution--");
                pw.println("Gender,Count");
                List<PatientGenderStatsResponse> genders = statsService.getPatientGenderStats(fromDate, toDate);
                if (genders != null) {
                    for (PatientGenderStatsResponse r : genders) {
                        pw.printf("%s,%d\n", r.getGender() == null ? "" : r.getGender(), r.getCount() == null ? 0L : r.getCount());
                    }
                }
                pw.println();
                pw.println("--Age Groups--");
                pw.println("AgeGroup,Count");
                List<PatientAgeGroupStatsResponse> ages = statsService.getPatientAgeGroupStats(fromDate, toDate);
                if (ages != null) {
                    for (PatientAgeGroupStatsResponse r : ages) {
                        pw.printf("%s,%d\n", r.getAgeGroup() == null ? "" : r.getAgeGroup(), r.getCount() == null ? 0L : r.getCount());
                    }
                }
                pw.println();
                pw.println("--By Specialty--");
                pw.println("Specialty,Count");
                List<PatientSpecialtyStatsResponse> specs = statsService.getPatientSpecialtyStats(fromDate, toDate);
                if (specs != null) {
                    for (PatientSpecialtyStatsResponse r : specs) {
                        pw.printf("%s,%d\n", r.getSpecialtyName() == null ? "" : r.getSpecialtyName(), r.getCount() == null ? 0L : r.getCount());
                    }
                }

            } else if ("disease".equalsIgnoreCase(type)) {
                pw.println("Specialty,Count");
                List<PatientSpecialtyStatsResponse> specs = statsService.getPatientSpecialtyStats(fromDate, toDate);
                if (specs != null) {
                    for (PatientSpecialtyStatsResponse r : specs) {
                        pw.printf("%s,%d\n", r.getSpecialtyName() == null ? "" : r.getSpecialtyName(), r.getCount() == null ? 0L : r.getCount());
                    }
                }

            } else if ("revenue".equalsIgnoreCase(type)) {
                int selYear = yearValue != null ? yearValue : LocalDate.now().getYear();
                int selMonth = month != null ? month : 0;
                pw.printf("Report for Year,%d,Month,%d\n", selYear, selMonth);
                pw.println();
                pw.println("--Revenue by Month--");
                pw.println("Month,Amount");
                List<RevenueStatsResponse> revs = statsService.getRevenueStatsByYear(selYear);
                if (revs != null) {
                    for (RevenueStatsResponse r : revs) {
                        pw.printf("%d,%s\n", r.getMonth(), r.getAmount() == null ? "0" : r.getAmount().toString());
                    }
                }
                pw.println();
                pw.println("--Revenue by Specialty--");
                pw.println("Specialty,Amount");
                List<RevenueBySpecialtyStatsResponse> rbs = statsService.statsRevenueBySpecialty(selYear, selMonth);
                if (rbs != null) {
                    for (RevenueBySpecialtyStatsResponse r : rbs) {
                        pw.printf("%s,%s\n", r.getSpecialty() == null ? "" : r.getSpecialty(), r.getAmount() == null ? "0" : r.getAmount().toString());
                    }
                }
                pw.println();
                pw.println("--Revenue by Type--");
                pw.println("Type,Amount");
                List<RevenueByTypeStatsResponse> rbt = statsService.statsRevenueByType(selYear, selMonth);
                if (rbt != null) {
                    for (RevenueByTypeStatsResponse r : rbt) {
                        pw.printf("%s,%s\n", r.getType() == null ? "" : r.getType(), r.getAmount() == null ? "0" : r.getAmount().toString());
                    }
                }

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Export for this report type is not implemented");
            }
        }
    }
    
    
    

   
}
