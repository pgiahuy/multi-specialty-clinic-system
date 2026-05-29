/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.LabTestResponse;
import com.hb.pojo.LabTests;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author DELL
 */
@Mapper(componentModel = "spring")
public interface LabTestMapper {
    LabTestMapper INSTANCE = Mappers.getMapper(LabTestMapper.class);
    
    LabTestResponse toResponse(LabTests test);
}
