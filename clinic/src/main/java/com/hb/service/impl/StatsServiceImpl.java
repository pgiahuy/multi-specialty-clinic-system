/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.response.stats.PatientAgeGroupStatsResponse;
import com.hb.dto.response.stats.PatientGenderStatsResponse;
import com.hb.dto.response.stats.PatientSpecialtyStatsResponse;
import com.hb.dto.response.stats.RevenueBySpecialtyStatsResponse;
import com.hb.dto.response.stats.RevenueByTypeStatsResponse;
import com.hb.dto.response.stats.RevenueStatsResponse;
import com.hb.dto.response.stats.ServiceUsageStatsResponse;
import com.hb.repository.StatsRepository;
import com.hb.service.StatsService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */
@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsRepository statsRepo;

    @Override
    public List<PatientGenderStatsResponse> getPatientGenderStats(LocalDate fromDate, LocalDate toDate) {
        List<Object[]> res = this.statsRepo.countPatientsByGender(fromDate, toDate);
        return res.stream()
                .map(obj -> 
                        new PatientGenderStatsResponse((String) obj[0],(Long) obj[1])
                ).toList();
    }

    @Override
    public List<PatientAgeGroupStatsResponse> getPatientAgeGroupStats(LocalDate fromDate, LocalDate toDate) {
        List<Object[]> res = this.statsRepo.countPatientsByAgeGroup(fromDate, toDate);
        java.util.Map<String, Long> grouped = new java.util.LinkedHashMap<>();
        res.forEach(obj -> {
            LocalDate dob = (LocalDate) obj[0];
            Long count = (Long) obj[1];
            int age = java.time.Period.between(dob, LocalDate.now()).getYears();
            String ageGroup;
            if (age < 18) {
                ageGroup = "Dưới 18";
            } else if (age < 30) {
                ageGroup = "18-29";
            } else if (age < 45) {
                ageGroup = "30-44";
            } else if (age < 60) {
                ageGroup = "45-59";
            } else {
                ageGroup = "60+";
            }
            grouped.merge(ageGroup, count, Long::sum);
        });
        return grouped.entrySet().stream()
                .map(entry -> new PatientAgeGroupStatsResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    public List<PatientSpecialtyStatsResponse> getPatientSpecialtyStats(LocalDate fromDate, LocalDate toDate) {
        List<Object[]> res = this.statsRepo.countPatientsBySpecialty(fromDate, toDate);
        return res.stream()
                .map(obj -> 
                        new PatientSpecialtyStatsResponse(String.valueOf(obj[0]), (Long) obj[1])
                ).toList();
    }

    @Override
    public List<RevenueStatsResponse> getRevenueStatsByYear(int year) {
        List<Object[]> res = this.statsRepo.statsRevenueByYear(year);
        return res.stream()
                .map(obj ->
                        new RevenueStatsResponse((int) obj[0], (BigDecimal) obj[1])
                ).toList();
    }

    @Override
    public List<RevenueBySpecialtyStatsResponse> statsRevenueBySpecialty(int year, int month) {
        List<Object[]> res = this.statsRepo.statsRevenueBySpecialty(year, month);
        return res.stream()
                .map(obj -> new RevenueBySpecialtyStatsResponse((String) obj[0], (BigDecimal) obj[1]))
                .toList();
    }

    @Override
    public List<RevenueByTypeStatsResponse> statsRevenueByType(int year, int month) {
        List<Object[]> res = this.statsRepo.statsRevenueByType(year, month);
        return res.stream()
                .map(obj -> {
                    String typeKey = String.valueOf(obj[0]);
                    String typeLabel;
                    switch (typeKey) {
                        case "APPOINTMENT":
                            typeLabel = "Khám bệnh";
                            break;
                        case "LAB_TEST":
                            typeLabel = "Xét nghiệm";
                            break;
                        case "PRESCRIPTION":
                            typeLabel = "Đơn thuốc";
                            break;
                        default:
                            typeLabel = typeKey;
                    }
                    return new RevenueByTypeStatsResponse(typeLabel, (BigDecimal) obj[1]);
                })
                .toList();
    }

    @Override
    public List<ServiceUsageStatsResponse> statsServiceUsage(LocalDate fromDate, LocalDate toDate) {
        List<Object[]> res = this.statsRepo.serviceUsageStats(fromDate, toDate);
         return res.stream()
                .map(obj -> {
                    String typeKey = String.valueOf(obj[0]);
                    String typeLabel;
                    switch (typeKey) {
                        case "APPOINTMENT":
                            typeLabel = "Khám bệnh";
                            break;
                        case "LAB_TEST":
                            typeLabel = "Xét nghiệm";
                            break;
                        case "PRESCRIPTION":
                            typeLabel = "Đơn thuốc";
                            break;
                        default:
                            typeLabel = typeKey;
                    }
                    return new ServiceUsageStatsResponse(typeLabel, (Long) obj[1]);
                })
                .toList();
    }

}
