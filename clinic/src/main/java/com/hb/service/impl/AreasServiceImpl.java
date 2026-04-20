/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.pojo.Areas;
import com.hb.repository.AreasRepository;
import com.hb.service.AreasService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class AreasServiceImpl implements AreasService {

    @Autowired
    private AreasRepository areaRepo;

    @Override
    public List<Areas> getAreas(Map<String, String> params) {
        return areaRepo.getAreas(params);
    }

    @Override
    public Areas getAreasById(Long id) {
        Areas a = areaRepo.getAreasById(id);
        if (a == null) {
            throw new RuntimeException("Doctor not found!");
        }
        return a;
    }

    @Override
    public Areas addArea(Map<String, String> params) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAreas(Long id) {
        this.areaRepo.deleteAreas(id);
    }
    @Override
    public long countAreas(Map<String, String> params) {
        return areaRepo.count(params, Areas.class);
    }
}
