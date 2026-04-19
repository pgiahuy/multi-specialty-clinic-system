/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.Areas;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface AreasService {
    List<Areas> getAreas(Map<String,String> params);
    Areas getAreasById(Long id);
    Areas addArea(Map<String,String> params);
    void deleteAreas(Long id);
}
