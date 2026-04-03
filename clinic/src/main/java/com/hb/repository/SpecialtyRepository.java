/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Specialtie;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface SpecialtieRepository {
    List<Specialtie> getSpecialties(Map<String,String> params);
    Specialtie getSpecialtieById(Long id);
    Specialtie addSpecialtie(Specialtie s);
    
}
