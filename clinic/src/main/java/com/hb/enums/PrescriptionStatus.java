/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.enums;

/**
 *
 * @author HUY
 */
public enum PrescriptionStatus {
    DRAFT("Bản nháp"),
    PUBLIC("Chính thức"),
    CANCELLED("Đã huỷ");
    
    private final String label;

    PrescriptionStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    
}
