package com.hb.controllers.api;

import com.hb.dto.response.RoomResponse;
import com.hb.pojo.Rooms;
import com.hb.service.RoomService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@PropertySource("classpath:configs.properties")
@CrossOrigin
public class ApiRoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private Environment env;

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomResponse>> list(@RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));

        List<Rooms> rooms = roomService.getRooms(params);
        List<RoomResponse> response = rooms.stream().map(room -> new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getAreaId() != null ? room.getAreaId().getAreaName() : null,
                room.getAreaId() != null ? room.getAreaId().getLocationFloor() : null
        )).toList();

        return ResponseEntity.ok(response);
    }
}