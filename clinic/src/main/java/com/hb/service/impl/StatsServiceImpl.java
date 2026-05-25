/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.response.stats.PatientAgeGroupStatsResponse;
import com.hb.dto.response.stats.PatientGenderStatsResponse;
import com.hb.dto.response.stats.PatientSpecialtyStatsResponse;
import com.hb.repository.StatsRepository;
import com.hb.service.StatsService;
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
        return res.stream()
                .map(obj -> 
                        new PatientAgeGroupStatsResponse((String) obj[0],(Long) obj[1])
                ).toList();
    }

    @Override
    public List<PatientSpecialtyStatsResponse> getPatientSpecialtyStats(LocalDate fromDate, LocalDate toDate) {
        List<Object[]> res = this.statsRepo.countPatientsBySpecialty(fromDate, toDate);
        return res.stream()
                .map(obj -> 
                        new PatientSpecialtyStatsResponse((String) obj[0],(Long) obj[1])
                ).toList();
    }

}
