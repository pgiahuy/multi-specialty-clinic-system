/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.MedicalRecordResponse;
import com.hb.pojo.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author HUY
 */

@Mapper
public interface MedicalRecordMapper {
    MedicalRecordMapper INSTANCE = Mappers.getMapper(MedicalRecordMapper.class);

    @Mapping(source = "appointmentId.patientId.fullName", target = "patientName")
    MedicalRecordResponse toResponse(MedicalRecord medicalRecord);
    
}
