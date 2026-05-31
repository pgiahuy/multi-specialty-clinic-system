/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.PaymentResponse;
import com.hb.pojo.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author DELL
 */
@Mapper(componentModel = "spring", uses = {PaymentItemMapper.class})
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);
    
    @Mapping(source = "appointmentId.patientId.fullName", target = "patientName")
    @Mapping(source = "appointmentId.id", target = "appointmentId")        
    PaymentResponse toResponse(Payment payment);
    
}
