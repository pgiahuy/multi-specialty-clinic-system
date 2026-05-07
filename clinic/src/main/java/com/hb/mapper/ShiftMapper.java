/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.ShiftResponse;
import com.hb.pojo.Shifts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author HUY
 */

@Mapper
public interface ShiftMapper {
    
    ShiftMapper INSTANCE = Mappers.getMapper(ShiftMapper.class);

    @Mapping(source = "session.label", target = "session")
    ShiftResponse toResponse(Shifts shifts);
    
}
