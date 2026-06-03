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
    List<Object[]> topDiseasesStats(LocalDate fromDate, LocalDate toDate,int limit);
    List<Object[]> medicineInventoryStats(LocalDate fromDate, LocalDate toDate);
    List<Object[]> statsRevenueByYear(int year);
    List<Object[]> statsRevenueBySpecialty(int year, int month);
    List<Object[]> statsRevenueByType(int year, int month);
    List<Object[]> revenueDetailsStats(LocalDate fromDate, LocalDate toDate);
}