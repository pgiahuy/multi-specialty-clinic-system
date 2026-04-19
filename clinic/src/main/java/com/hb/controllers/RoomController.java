/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.service.RoomService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author DELL
 */
@Controller
@RequestMapping("/admin")
public class RoomController {
    @Autowired
    private RoomService roomService;
    
    @GetMapping("/rooms")
    public String list(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("rooms", roomService.getRooms(params));
        return "room";
    }
    

    @PostMapping("/rooms")
    public String create(@RequestParam Map<String, String> params) {

        roomService.addRoom(params);
        return "redirect:/admin/room";
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
        }
    }
}
