/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.service;

import com.hb.pojo.LabTests;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public interface LabTestService {
    List<LabTests> getLabTests(Map<String, String> params);
    LabTests getLabTestById(Integer id);
    void addOrUpdateLabTest(Map<String, String> params);
    void deleteLabTest(Integer id);
    long countLabTests(Map<String, String> params);
}
