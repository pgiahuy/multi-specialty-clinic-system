/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.PrescriptionItem;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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