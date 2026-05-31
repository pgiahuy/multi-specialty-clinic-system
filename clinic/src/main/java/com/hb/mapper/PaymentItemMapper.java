/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.mapper;

import com.hb.dto.response.PaymentItemResponse;
import com.hb.pojo.PaymentItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author DELL
 */
@Mapper(componentModel = "spring")
public interface PaymentItemMapper {
   PaymentItemMapper INSTANCE = Mappers.getMapper(PaymentItemMapper.class);
   
   @Mapping(source = "paymentId.id", target = "paymentId")
   PaymentItemResponse toResponse(PaymentItems paymentItem);
}
