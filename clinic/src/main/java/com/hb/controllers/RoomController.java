/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.controllers;

import com.hb.dto.request.form.RoomForm;
import com.hb.service.AreasService;
import com.hb.service.RoomService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.stream.Collectors;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author DELL
 */


@Controller
@RequestMapping("/admin")
@PropertySource("classpath:configs.properties")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private AreasService areasService;
    @Autowired
    private Environment  env;
    @GetMapping("/rooms")
    public String list(Model model, @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));

        model.addAttribute("rooms", roomService.getRooms(params));
        model.addAttribute("roomForm", new RoomForm());
        long totalRooms = roomService.countRooms(params);
        int totalPages = (int) Math.ceil((double) totalRooms / pageSize);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "room";
    }
    

    @PostMapping("/rooms")
    public String create(@ModelAttribute RoomForm roomForm) {
        roomService.saveOrUpdate(roomForm);
        return "redirect:/admin/rooms";
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
        }
    }

    @GetMapping("/rooms/search-areas")
    @ResponseBody
    public List<Map<String, Object>> searchAreas(@RequestParam(value = "kw", required = false) String kw) {
        Map<String, String> params = new HashMap<>();
        if (kw != null && !kw.trim().isEmpty()) {
            params.put("kw", kw.trim());
        }

        return this.areasService.getAreas(params).stream()
                .map(a -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", a.getId());
                    item.put("name", a.getAreaName());
                    return item;
                })
                .collect(Collectors.toList());
    }
}
