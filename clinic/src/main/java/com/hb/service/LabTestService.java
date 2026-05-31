/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.LabTest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface LabTestService {
    List<LabTest> getLabTests(Map<String, String> params);
    LabTest getLabTestById(Long id);
    void addOrUpdateLabTest(Map<String, String> params);
    void deleteLabTest(Long id);
    long countLabTests(Map<String, String> params);
}
