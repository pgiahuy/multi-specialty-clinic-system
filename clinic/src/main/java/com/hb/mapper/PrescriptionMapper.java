/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.PrescriptionResponse;
import com.hb.pojo.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author HUY
 */
@Mapper(uses = PrescriptionItemMapper.class)
public interface PrescriptionMapper {
    
    PrescriptionMapper INSTANCE = Mappers.getMapper(PrescriptionMapper.class);

    @Mapping(source = "medicalRecordId.appointmentId.scheduleId.doctorId.fullName", target = "doctorName")
    @Mapping(source = "medicalRecordId.appointmentId.patientId.fullName", target = "patientName")
    @Mapping(source = "prescriptionItemCollection", target = "items")
    @Mapping(source = "medicalRecordId.note", target = "note")
    PrescriptionResponse toResponse(Prescription prescription);
    
}
