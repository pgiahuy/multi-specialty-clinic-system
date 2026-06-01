/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.enums;

/**
 *
 * @author HUY
 */
public enum UserRole {
    
    ROLE_PATIENT("Bệnh nhân"),
    ROLE_DOCTOR("Bác sĩ"),
    ROLE_ADMIN("Quản trị viên"),
    ROLE_STAFF("Nhân viên y tế"),
    ROLE_STOREKEEPER("Thủ kho");
    
    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
