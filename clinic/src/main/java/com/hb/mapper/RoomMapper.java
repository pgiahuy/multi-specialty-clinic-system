/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.RoomResponse;
import com.hb.pojo.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author HUY
 */
@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    @Mapping(source = "areaId.areaName", target = "areaName")
    @Mapping(source = "areaId.locationFloor", target = "locationFloor")
    @Mapping(source = "specialtyId.id", target = "specialtyId")
    @Mapping(source = "specialtyId.name", target = "specialtyName")
    RoomResponse toResponse(Room room);

}
