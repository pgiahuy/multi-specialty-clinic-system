/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.enums;

/**
 *
 * @author HUY
 */
public enum NotificationType {
    PRESCRIPTION("Đơn thuốc"),
    APPOINTMENT("Lịch hẹn"),
    SYSTEM("Hệ thống");
    private final String label;

    NotificationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
