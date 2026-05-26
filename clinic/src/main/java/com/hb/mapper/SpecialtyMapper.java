/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.SpecialtyResponse;
import com.hb.pojo.Specialty;
import org.springframework.stereotype.Component;

/**
 *
 * @author DELL
 */
@Component
public class SpecialtyMapper {
    public SpecialtyResponse toResponse(Specialty s) {
        SpecialtyResponse res = new SpecialtyResponse();
        res.setId(s.getId());
        res.setName(s.getName());
        res.setFee(s.getPrice());
        
        if(s.getIdHod()!=null) {
            res.setNameHod(s.getIdHod().getFullName());
        }
        
        return res;
    }
}
