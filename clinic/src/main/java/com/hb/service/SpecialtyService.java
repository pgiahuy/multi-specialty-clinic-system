/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.form.SpecialtyForm;
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
    Specialty saveOrUpdate(SpecialtyForm form);
    void deleteSpecialty(Long id);
    long countSpecialties(Map<String,String> params);
}
