/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import java.util.Date;
import java.util.List;

/**
 *
 * @author HUY
 */
public interface StatsRepository {
    
    List<Object[]> countPatientsByGender();
    List<Object[]> countPatientsByAgeGroup();
    List<Object[]> countPatientsBySpecialty();


    List<Object[]> serviceUsageStats(Date fromDate, Date toDate);


    List<Object[]> topDiseasesStats(int limit);


    List<Object[]> revenueStats(int year);
    List<Object[]> revenueDetailsStats(Date fromDate, Date toDate);
}