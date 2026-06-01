/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.LabResultDetailResponse;
import com.hb.pojo.LabResultDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author DELL
 */
@Mapper(componentModel = "spring")
public interface LabResultDetailMapper {

    LabResultDetailMapper INSTANCE = Mappers.getMapper(LabResultDetailMapper.class);

    @Mapping(source = "testId.testName", target = "testName")
    @Mapping(source = "testId.normalRange", target = "normalRange")
    @Mapping(source = "testId.unit", target = "unit")
    LabResultDetailResponse toResponse(LabResultDetail labResultDetail);
}
