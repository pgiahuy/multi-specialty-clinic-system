/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.PrescriptionItem;
import java.util.List;

/**
 *
 * @author HUY
 */

public interface PrescriptionItemRepository {

    PrescriptionItem save(PrescriptionItem item);

    PrescriptionItem getById(Long id);

    List<PrescriptionItem> getByPrescriptionId(Long prescriptionId);

    void delete(PrescriptionItem item);
}