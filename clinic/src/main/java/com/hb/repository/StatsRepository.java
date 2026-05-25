/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HUY
 */
public interface StatsRepository {
    
    List<Object[]> countPatientsByGender(LocalDate fromDate, LocalDate toDate);
    List<Object[]> countPatientsByAgeGroup(LocalDate fromDate, LocalDate toDate);
    List<Object[]> countPatientsBySpecialty(LocalDate fromDate, LocalDate toDate);


    List<Object[]> serviceUsageStats(LocalDate fromDate, LocalDate toDate);


    List<Object[]> topDiseasesStats(int limit);


    List<Object[]> revenueStats(int year);
    List<Object[]> revenueDetailsStats(LocalDate fromDate, LocalDate toDate);
}