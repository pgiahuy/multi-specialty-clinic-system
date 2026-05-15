/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hb.repository;

import com.hb.pojo.LabResults;

/**
 *
 * @author DELL
 */
public interface LabTestResultRepository {
    LabResults getLabResultById(Long id);
    void addOrUpdateTestResult(LabResults lr);
}
