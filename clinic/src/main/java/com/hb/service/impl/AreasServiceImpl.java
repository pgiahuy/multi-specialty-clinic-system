/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.form.AreaForm;
import com.hb.pojo.Area;
import com.hb.service.AreasService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hb.repository.AreaRepository;

/**
 *
 * @author DELL
 */
@Service
public class AreasServiceImpl implements AreasService {

    @Autowired
    private AreaRepository areaRepo;

    @Override
    public List<Area> getAreas(Map<String, String> params) {
        return areaRepo.getAreas(params);
    }

    @Override
    public Area getAreasById(Long id) {
        Area a = areaRepo.getAreasById(id);
        if (a == null) {
            throw new RuntimeException("Doctor not found!");
        }
        return a;
    }

    @Override
    public void deleteAreas(Long id) {
        this.areaRepo.deleteAreas(id);
    }

    @Override
    public long countAreas(Map<String, String> params) {
        return areaRepo.count(params, Area.class);
    }

    @Override
    public Area saveOrUpdate(AreaForm form) {
        Area a;
        if (form.getId() == null) {
            a = new Area();
        } else {
            a = this.areaRepo.getAreasById(form.getId());
        }

        a.setAreaName(form.getAreaName() != null ? form.getAreaName() : null);
        a.setLocationFloor(form.getLocationFloor() != null ? form.getLocationFloor() : null);
        
        return this.areaRepo.saveOrUpdate(a);
    }
}
