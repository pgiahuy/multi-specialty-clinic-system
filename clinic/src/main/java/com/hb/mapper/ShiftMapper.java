/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.ShiftResponse;
import com.hb.pojo.Shift;
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


    @Mapping(target = "sessionCode", expression = "java(shift.getSession() != null ? shift.getSession().name() : null)")
    @Mapping(source = "session.label", target = "session")
    ShiftResponse toResponse(Shift shift);
    
}
