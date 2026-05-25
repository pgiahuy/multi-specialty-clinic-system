/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.response.stats.PatientAgeGroupStatsResponse;
import com.hb.dto.response.stats.PatientGenderStatsResponse;
import com.hb.dto.response.stats.PatientSpecialtyStatsResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */

    
public interface StatsService {
    List<PatientGenderStatsResponse> getPatientGenderStats(LocalDate fromDate, LocalDate toDate);
    List<PatientAgeGroupStatsResponse> getPatientAgeGroupStats(LocalDate fromDate, LocalDate toDate);
    List<PatientSpecialtyStatsResponse> getPatientSpecialtyStats(LocalDate fromDate, LocalDate toDate);
}
