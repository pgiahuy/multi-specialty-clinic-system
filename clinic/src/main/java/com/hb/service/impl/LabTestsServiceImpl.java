/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.pojo.LabTests;
import com.hb.repository.LabTestRepository;
import com.hb.service.LabTestService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class LabTestsServiceImpl implements LabTestService {

    @Autowired
    private LabTestRepository labTestRepo;

    @Override
    public List<LabTests> getLabTests(Map<String, String> params) {
        return labTestRepo.getLabTests(params);
    }

    @Override
    public LabTests getLabTestById(Integer id) {
        return labTestRepo.getLabTestById(id);
    }
   

    @Override
    public void deleteLabTest(Integer id) {
        labTestRepo.deleteLabTest(id);
    }

    @Override
    public void addOrUpdateLabTest(Map<String, String> params) {
        LabTests t = new LabTests();
        String id = params.get("id");
        if (id != null && !id.isEmpty()) {
            t.setId(Integer.parseInt(id));
        }

        t.setTestName(params.get("test_name"));
        t.setUnit(params.get("unit"));
        t.setNormalRange(params.get("normal_range"));

        this.labTestRepo.addOrUpdateLabTest(t);
    }

    @Override
    public long countLabTests(Map<String, String> params) {
        return labTestRepo.count(params, LabTests.class);
    }

}
