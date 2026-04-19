/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Specialty;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HUY
 */
public interface SpecialtyRepository extends BaseRepository<Specialty>{
    List<Specialty> getSpecialties(Map<String,String> params);
    Specialty getSpecialtieById(Long id);
    Specialty addSpecialtie(Specialty s);
    
}
