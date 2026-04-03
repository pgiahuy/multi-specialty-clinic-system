/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.pojo.Appointment;
import com.hb.service.AppointmentService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HUY
 */

@Service
public class AppointmentServiceImpl implements AppointmentService{
    
    @Autowired
    private AppointmentService appointmentService;

    @Override
    public List<Appointment> getAppointments(Map<String, String> params) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        Appointment a =  appointmentService.getAppointmentById(id);
        if(a== null){
            throw new RuntimeException("Appointment not found!");
        }
        return a;
    }

    @Override
    public Appointment addAppointment(Map<String, String> params) {
        return null;
    }
    
}
