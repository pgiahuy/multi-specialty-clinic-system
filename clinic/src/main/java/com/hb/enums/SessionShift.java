/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.enums;

/**
 *
 * @author HUY
 */
public enum SessionShift {
    MORNING("Sáng"),
    AFTERNOON("Chiều"),
    EVENING("Tối");

    private final String label;

    SessionShift(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
