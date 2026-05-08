/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.PrescriptionItem;
import com.hb.repository.PrescriptionItemRepository;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HUY
 */
@Repository
public class PrescriptionItemRepositoryImpl implements PrescriptionItemRepository {

    @Autowired
    private Environment evn;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public PrescriptionItem save(PrescriptionItem item) {
        Session session = factory.getObject().getCurrentSession();
        if (item.getId() == null) {
            session.persist(item);
        } else {
            // id not null -> decide between persist/merge by checking existence
            if (session.find(PrescriptionItem.class, item.getId()) == null) {
                session.persist(item);
            } else {
                session.merge(item);
            }
        }
        return item;
    }

    @Override
    public PrescriptionItem getById(Long id) {
        Session session = factory.getObject().getCurrentSession();
        return session.get(PrescriptionItem.class, id);
    }

    @Override
    public List<PrescriptionItem> getByPrescriptionId(Long prescriptionId) {
        Session session = factory.getObject().getCurrentSession();

        return session.createQuery(
                "FROM PrescriptionItem i WHERE i.prescriptionId.id = :id",
                PrescriptionItem.class
        )
                .setParameter("id", prescriptionId)
                .getResultList();
    }

    @Override
    public void delete(PrescriptionItem item) {
        Session session = factory.getObject().getCurrentSession();
        session.detach(item);
    }

}
