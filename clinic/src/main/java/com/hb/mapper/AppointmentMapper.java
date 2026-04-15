/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.request.AppointmentCreateRequest;
import com.hb.dto.response.AppointmentResponse;
import com.hb.pojo.Appointment;

/**
 *
 * @author HUY
 */


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    AppointmentResponse toResponse(Appointment a);

    Appointment toEntity(AppointmentCreateRequest request);

    void updateFromRequest(AppointmentCreateRequest req,
                           @MappingTarget Appointment appointment);
}