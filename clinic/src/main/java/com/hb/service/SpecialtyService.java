/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.Specialtie;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface SpecialtieService {
    List<Specialtie> getSpecialties(Map<String,String> params);
    Specialtie getSpecialtieById(Long id);
    Specialtie addSpecialtie(Map<String, String> params);
}
