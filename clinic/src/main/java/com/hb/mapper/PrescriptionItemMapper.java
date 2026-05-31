/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.PrescriptionItemResponse;
import com.hb.pojo.PrescriptionItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author HUY
 */

@Mapper
public interface PrescriptionItemMapper {
    PrescriptionItemMapper INSTANCE = Mappers.getMapper(PrescriptionItemMapper.class);

    @Mapping(source = "medicineId.name", target = "medicineName")
    @Mapping(source = "medicineId.secureUrl", target = "medicineImage")
    @Mapping(source = "medicineId.price", target = "medicinePrice")
    @Mapping(source = "medicineId.id", target = "medicineId")
    @Mapping(source = "medicineId.unit", target = "unit")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "daysToUse", target = "daysToUse")
    @Mapping(source = "quantity", target = "quantity")
    PrescriptionItemResponse toResponse(PrescriptionItem prescriptionItem);
}
