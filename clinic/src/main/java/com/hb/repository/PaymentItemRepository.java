/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.PaymentItems;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface PaymentItemRepository {
    List<PaymentItems> getItemsByPaymentId(Long paymentId);
    PaymentItems getItemById(Long id);
    void addOrUpdateItem(PaymentItems item);
    void deleteItem(Long id);
}
