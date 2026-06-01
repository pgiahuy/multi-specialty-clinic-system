package com.hb.controllers.api;

import com.hb.dto.response.RoomResponse;
import com.hb.exception.ResourceNotFoundException;
import com.hb.enums.UserRole;
import com.hb.pojo.Room;
import com.hb.pojo.User;
import com.hb.service.RoomService;
import com.hb.service.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private UserService userService;

    @Autowired
    private Environment env;

    @GetMapping("/rooms")
    @PreAuthorize("hasAnyRole('DOCTOR','STAFF')")
    public ResponseEntity<List<RoomResponse>> list(@RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;

        int pageSize = this.env.getProperty("admin.page_size", Integer.class);
        params.put("pageSize", String.valueOf(pageSize));

        List<Room> rooms = roomService.getRooms(params);
        List<RoomResponse> response = rooms.stream().map(room -> new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getAreaId() != null ? room.getAreaId().getAreaName() : null,
                room.getAreaId() != null ? room.getAreaId().getLocationFloor() : null
        )).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/secure/available-rooms")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<RoomResponse>> list(@RequestParam Map<String, String> params, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.getUserByUsername(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (user.getRole() == null || !UserRole.ROLE_DOCTOR.equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<RoomResponse> rooms = roomService.getAvailableRoomsForDoctor(params, user);
        
        return ResponseEntity.ok(rooms);
    }
}
