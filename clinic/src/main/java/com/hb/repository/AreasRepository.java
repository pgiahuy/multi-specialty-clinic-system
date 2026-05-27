/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Areas;
import com.hb.pojo.Specialty;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface AreasRepository extends BaseRepository<Areas>{
    List<Areas> getAreas(Map<String,String> params);
    Areas getAreasById(Long id);
    Areas saveOrUpdate(Areas a);
    void deleteAreas(Long id);
}
