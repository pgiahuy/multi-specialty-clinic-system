package com.hb.controllers.api;

import com.hb.dto.request.ChatMessageCreateRequest;
import com.hb.dto.response.ChatMessageResponse;
import com.hb.enums.UserRole;
import com.hb.pojo.User;
import com.hb.service.ChatService;
import com.hb.service.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/secure/chat-messages")
@CrossOrigin(origins = "*")
public class ApiChatMessageController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @GetMapping("/conversation/{conversationId}")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT')")
    public ResponseEntity<List<ChatMessageResponse>> getMessagesByConversation(@PathVariable("conversationId") Long conversationId) {
        List<ChatMessageResponse> responses = chatService.getMessagesByConversation(conversationId);
        return ResponseEntity.ok(responses);
    }

//conversation_id, sender_id,content,message_type,sender_type
    
    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT')")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageCreateRequest request, Principal principal) {
        String senderType = userService.getRoleByUsername(principal.getName());
        try {
            Long conversationId = request.getConversationId();
            Long senderId = request.getSenderId();

            if (senderId == null) {
                if (principal == null || principal.getName() == null) {
                    return ResponseEntity.status(401).body("Người dùng chưa xác thực!");
                }
                senderId = userService.getUserByUsername(principal.getName()).getId();
            }
            String content = request.getContent();
            String msgType = request.getMessageType();

            if (conversationId == null) {
                return ResponseEntity.badRequest().body("conversationId không được để trống");
            }
            if (senderType == null || (!UserRole.ROLE_DOCTOR.toString().equals(senderType)
                    && !UserRole.ROLE_PATIENT.toString().equals(senderType))) {
                return ResponseEntity.badRequest().body("Người gửi phải là Bác sĩ hoặc bệnh nhân!");
            }

            ChatMessageResponse response = chatService.saveAndPushMessage(conversationId, senderId, content, msgType, senderType);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
