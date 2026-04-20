/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository.impl;

import com.hb.pojo.Specialty;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.hb.repository.SpecialtyRepository;

/**
 *
 * @author HUY
 */

@Repository
@Transactional
public class SpecialtyRepositoryImpl extends BaseRepositoryImpl<Specialty> implements SpecialtyRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;
    

    @Override
    public List<Specialty> getSpecialties(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Specialty> q = session.createNamedQuery("Specialty.findAllWithDoctors",Specialty.class);
        
        if(params!= null){
            int pageSize = Integer.parseInt(params.get("pageSize"));
            System.out.println("===========" );
            System.out.println(pageSize );
            int page = Integer.parseInt( params.getOrDefault("page", "1"));
            int start = (page-1)*pageSize;
            
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
            
        }
        System.out.println("=======print_list==============");
        System.out.println(q.getResultList());
                System.out.println("=======print_list==============");

        return q.getResultList();
    }

    @Override
    public Specialty getSpecialtieById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Specialty> q = session.createNamedQuery("Specialty.findById", Specialty.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    @Override
    public Specialty addSpecialtie(Specialty s) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(s);
        return s;
    }
    
}
