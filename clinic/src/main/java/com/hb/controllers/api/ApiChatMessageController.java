package com.hb.controllers.api;

import com.hb.enums.UserRole;
import com.hb.pojo.ChatMessage;
import com.hb.pojo.User;
import com.hb.dto.response.ChatMessageResponse;
import com.hb.service.ChatService;
import com.hb.service.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
        List<ChatMessage> messages = chatService.getMessagesByConversation(conversationId);
        List<ChatMessageResponse> responses = messages.stream().map(msg
                -> new ChatMessageResponse(
                        msg.getId(),
                        msg.getConversationId() != null ? msg.getConversationId().getId() : null,
                        msg.getSenderId() != null ? msg.getSenderId().getId() : null,
                        msg.getSenderId() != null ? msg.getSenderId().getUsername() : null,
                        msg.getSenderType(),
                        msg.getContent(),
                        msg.getMessageType(),
                        msg.getCreatedAt()
                )
        ).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

//conversation_id, sender_id,content,message_type,sender_type
    
    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT')")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> params, Principal principal) {
        String senderType = userService.getRoleByUsername(principal.getName());
        try {
            Long conversationId = Long.valueOf(params.get("conversation_id"));
            Long senderId = null;
            String senderIdStr = params.get("sender_id");
            if (senderIdStr != null && !senderIdStr.trim().isEmpty()) {
                senderId = Long.valueOf(senderIdStr);
            } else {
                if (principal == null || principal.getName() == null) {
                    return ResponseEntity.status(401).body("Người dùng chưa xác thực!");
                }
                senderId = userService.getUserByUsername(principal.getName()).getId();
            }
            String content = params.get("content");
            String msgType = params.get("message_type");

            if (senderType == null || (!UserRole.ROLE_DOCTOR.toString().equals(senderType)
                    && !UserRole.ROLE_PATIENT.toString().equals(senderType))) {
                return ResponseEntity.badRequest().body("Người gửi phải là Bác sĩ hoặc bệnh nhân!");
            }

            ChatMessage savedMsg = chatService.saveAndPushMessage(conversationId, senderId, content, msgType, senderType);

            User sender = savedMsg.getSenderId();
            ChatMessageResponse response = new ChatMessageResponse(
                    savedMsg.getId(),
                    savedMsg.getConversationId() != null ? savedMsg.getConversationId().getId() : null,
                    sender != null ? sender.getId() : null,
                    sender != null ? sender.getUsername() : null,
                    savedMsg.getSenderType(),
                    savedMsg.getContent(),
                    savedMsg.getMessageType(),
                    savedMsg.getCreatedAt()
            );

            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Sai định dạng ID đầu vào (conversation_id hoặc sender_id phải là số)!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
