/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.Area;
import com.hb.pojo.Specialty;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface AreaRepository extends BaseRepository<Area>{
    List<Area> getAreas(Map<String,String> params);
    Area getAreasById(Long id);
    Area saveOrUpdate(Area a);
    void deleteAreas(Long id);
}
