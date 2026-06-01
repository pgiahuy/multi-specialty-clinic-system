/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.dto.request.form.AreaForm;
import com.hb.pojo.Area;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface AreasService {
    List<Area> getAreas(Map<String,String> params);
    Area getAreasById(Long id);
    Area saveOrUpdate(AreaForm form);
    void deleteAreas(Long id);
    long countAreas(Map<String,String> params);
}
