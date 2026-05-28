/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.LabTests;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface LabTestRepository extends BaseRepository<LabTests>{
    List<LabTests> getLabTests(Map<String, String> params);
    LabTests getLabTestById(Long id);
    void addOrUpdateLabTest(LabTests test);
    void deleteLabTest(Long id);
    Long countLabTests(Map<String, String> params);
}
