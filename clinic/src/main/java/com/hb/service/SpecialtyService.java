/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.Specialty;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface SpecialtyService {
    List<Specialty> getSpecialties(Map<String,String> params);
    Specialty getSpecialtieById(Long id);
    Specialty addSpecialtie(Map<String, String> params);
    void deleteDoctor(Long id);
    long countSpecialties(Map<String,String> params);
}
