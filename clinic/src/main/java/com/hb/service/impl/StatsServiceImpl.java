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
import com.hb.dto.response.stats.MedicineInventoryStatsResponse;
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
        
        // Dùng TreeMap<Integer, Long> để tự động sắp xếp tuổi tăng dần (1, 2, 3... 10, 20...)
        java.util.Map<Integer, Long> exactAgeMap = new java.util.TreeMap<>();
        
        res.forEach(obj -> {
            // Lấy ngày sinh và số lượng
            LocalDate dob = (LocalDate) obj[0];
            Long count = (Long) obj[1];
            
            // Tính tuổi chính xác
            int age = java.time.Period.between(dob, LocalDate.now()).getYears();
            
            // Gộp dữ liệu theo từng tuổi cụ thể (thay vì nhóm tuổi)
            exactAgeMap.merge(age, count, Long::sum);
        });
        
        // Chuyển đổi sang List DTO trả về cho Controller
        return exactAgeMap.entrySet().stream()
                // Gắn thêm chữ "tuổi" luôn vào DTO để JS không cần xử lý nữa
                .map(entry -> new PatientAgeGroupStatsResponse(entry.getKey() + " tuổi", entry.getValue()))
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

    @Override
    public List<com.hb.dto.response.stats.MedicineInventoryStatsResponse> getMedicineInventoryStats(LocalDate fromDate, LocalDate toDate) {
        List<Object[]> res = this.statsRepo.medicineInventoryStats(fromDate, toDate);
        return res.stream()
                .map(obj -> new com.hb.dto.response.stats.MedicineInventoryStatsResponse(
                        String.valueOf(obj[0]),
                obj[1] != null ? ((Number) obj[1]).longValue() : 0L,
                obj[2] != null ? ((Number) obj[2]).longValue() : 0L
                ))
                .toList();
    }

}
