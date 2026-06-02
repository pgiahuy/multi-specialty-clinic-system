/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.LabResultResponse;
import com.hb.pojo.LabResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author DELL
 */
@Mapper(componentModel = "spring", uses = {LabResultDetailMapper.class})
public interface LabResultMapper {
    LabResultMapper INSTANCE = Mappers.getMapper(LabResultMapper.class);
    
    @Mapping(source = "appointmentId.patientId.fullName", target = "patientName")
    @Mapping(source = "labResultDetailCollection", target = "resultDetails")
    @Mapping(source = "appointmentId.scheduleId.doctorId.fullName", target = "doctorName")
    @Mapping(source = "drId.fullName", target = "doctorTestName")
    LabResultResponse toResponse(LabResult labResult);
    
    
}
