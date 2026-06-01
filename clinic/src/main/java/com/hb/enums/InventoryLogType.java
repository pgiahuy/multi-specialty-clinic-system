/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.enums;

/**
 *
 * @author HUY
 */
public enum InventoryLogType {
    IMPORT_FROM_SUPPLIER("Nhập hàng"),
    PRESCRIPTION_EXPORT("Kê đơn"),
    EXPIRED_DISPOSAL("Huỷ hết hạn");
    
    private final String label;

    InventoryLogType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
