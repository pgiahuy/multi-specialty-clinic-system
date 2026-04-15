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
@PropertySource("classpath:configs.properties")
public class SpecialtyRepositoryImpl implements SpecialtyRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Autowired
    private Environment env;
    

    @Override
    public List<Specialty> getSpecialties(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Specialty> q = session.createNamedQuery("Specialtie.findAll",Specialty.class);
        
        if(params!= null){
            int pageSize = env.getProperty("specialties.page_size", Integer.class);
            int page = Integer.parseInt( params.getOrDefault("page", "1"));
            int start = (page-1)*pageSize;
            
            q.setMaxResults(pageSize);
            q.setFirstResult(start);
            
        }
        return q.getResultList();
    }

    @Override
    public Specialty getSpecialtieById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Specialty> q = session.createNamedQuery("Specialtie.findById", Specialty.class);
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
