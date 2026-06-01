package com.hb.controllers.api;

import com.hb.pojo.Conversation;
import com.hb.pojo.User;
import com.hb.pojo.Patient;
import com.hb.dto.response.ConversationResponse;
import com.hb.service.ChatService;
import com.hb.service.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/secure/conversations")
public class ApiConversationController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT')")
    public ResponseEntity<List<Map<String, Object>>> getConversations(@RequestParam Map<String,String> params, Principal principal) {
        try {
            if (principal == null || principal.getName() == null) {
                return ResponseEntity.status(401).build();
            }

            String role = userService.getRoleByUsername(principal.getName());
            User current = userService.getUserByUsername(principal.getName());

            if (role != null && role.equals("ROLE_DOCTOR")) {
                List<Map<String, Object>> conversations = chatService.getConversationsForDoctor(current.getId());
                return ResponseEntity.ok(conversations);
            } else if (role != null && role.equals("ROLE_PATIENT")) {
                List<Map<String, Object>> conversations = chatService.getConversationsForPatient(current.getId());
                return ResponseEntity.ok(conversations);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/quick-start")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT')")
    public ResponseEntity<?> quickStartConversation(@RequestBody Map<String, Long> params, Principal principal) {
        try {
            if (principal == null || principal.getName() == null) {
                return ResponseEntity.status(401).body("Người dùng chưa xác thực!");
            }

            String role = userService.getRoleByUsername(principal.getName());
            User current = userService.getUserByUsername(principal.getName());

            Long doctorId = params.get("doctorId");
            Long patientId = params.get("patientId");

            if (role != null && role.equals("ROLE_DOCTOR")) {
                doctorId = current.getId();
                if (patientId == null) {
                    return ResponseEntity.badRequest().body("Thiếu patientId trong request body!");
                }
            } else if (role != null && role.equals("ROLE_PATIENT")) {
                patientId = current.getId();
                if (doctorId == null) {
                    return ResponseEntity.badRequest().body("Thiếu doctorId trong request body!");
                }
            } else {
                return ResponseEntity.badRequest().body("Role không hợp lệ!");
            }

            Conversation conversation = chatService.quickStartConversation(doctorId, patientId);
            
            Patient patient = conversation.getPatientId();
            User receiver = conversation.getReceiverId();
            
            ConversationResponse response = new ConversationResponse(
                conversation.getId(),
                patient != null ? patient.getId() : null,
                patient != null ? patient.getFullName() : null,
                receiver != null ? receiver.getId() : null,
                receiver != null ? receiver.getUsername() : null,
                conversation.getAppointmentId() != null ? conversation.getAppointmentId().getId() : null,
                conversation.getConversationType(),
                conversation.getIsActive(),
                conversation.getCreatedAt()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
