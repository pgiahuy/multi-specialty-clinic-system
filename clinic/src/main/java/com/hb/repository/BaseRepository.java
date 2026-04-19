/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.repository;

import java.util.Map;

/**
 *
 * @author HUY
 */
public interface BaseRepository<T> {

    long count(Map<String, String> params, Class<T> clazz);
}
