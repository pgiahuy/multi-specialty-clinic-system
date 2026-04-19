/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.request.PatientCreateRequest;
import com.hb.dto.response.PatientResponse;
import com.hb.pojo.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 *
 * @author DELL
 */
@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.secureUrl", target = "avatar")
    @Mapping(source = "dob", target = "dob", dateFormat = "dd/MM/yyyy")
    PatientResponse toResponse(Patient patient);
    Patient toEntiy(PatientCreateRequest p);
    void updateFromRequest(PatientCreateRequest prq, @MappingTarget Patient patient);
}
