/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.service.RoomService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
@RequestMapping("/admin/rooms")
@PropertySource("classpath:configs.properties")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private Environment  env;
    @GetMapping("")
    public String list(Model model, @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));

        model.addAttribute("rooms", roomService.getRooms(params));

        long totalRooms = roomService.countRooms(params);
        int totalPages = (int) Math.ceil((double) totalRooms / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "room";
    }
    

    @PostMapping("")
    public String create(@RequestParam Map<String, String> params) {

        roomService.addRoom(params);
        return "redirect:/admin/room";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
        }
    }
}
