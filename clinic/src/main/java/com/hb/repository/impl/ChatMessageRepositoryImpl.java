package com.hb.repository.impl;

import com.google.api.core.ApiFuture;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import com.hb.pojo.ChatMessage;
import com.hb.pojo.Conversation;
import com.hb.pojo.User;
import com.hb.repository.ChatMessageRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * @author HUY
 */
@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private static final String COLLECTION_NAME = "chat_messages";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private DatabaseReference getDatabase() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public List<ChatMessage> findByConversationIdOrderByCreatedAtAsc(Long conversationId) {
        try {
            DatabaseReference ref = getDatabase().child(COLLECTION_NAME).child(String.valueOf(conversationId));
            Query query = ref.orderByChild("createdAt");

            final CountDownLatch latch = new CountDownLatch(1);
            final AtomicReference<DataSnapshot> snapshotRef = new AtomicReference<>();
            final AtomicReference<DatabaseError> errorRef = new AtomicReference<>();

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    snapshotRef.set(dataSnapshot);
                    latch.countDown();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    errorRef.set(databaseError);
                    latch.countDown();
                }
            });

            boolean awaited = latch.await(10, TimeUnit.SECONDS);
            if (!awaited) {
                throw new RuntimeException("Timed out waiting for Realtime Database response");
            }
            if (errorRef.get() != null) {
                throw new RuntimeException("Realtime Database error: " + errorRef.get().getMessage());
            }

            DataSnapshot snapshot = snapshotRef.get();

            List<ChatMessage> result = new ArrayList<>();
            if (snapshot != null && snapshot.exists()) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Object val = child.getValue();
                    if (val instanceof java.util.Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> data = (Map<String, Object>) val;
                        result.add(mapDocumentToChatMessage(child.getKey(), data));
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load chat messages from Realtime Database", e);
        }
    }

    @Override
    public ChatMessage save(ChatMessage msg) {
        try {
            if (msg.getCreatedAt() == null) {
                msg.setCreatedAt(LocalDateTime.now());
            }
            if (msg.getId() == null) {
                msg.setId(System.currentTimeMillis());
            }

            Map<String, Object> data = new HashMap<>();
            data.put("id", msg.getId());
            data.put("conversationId", msg.getConversationId() != null ? msg.getConversationId().getId() : null);
            data.put("senderId", msg.getSenderId() != null ? msg.getSenderId().getId() : null);
            data.put("senderType", msg.getSenderType());
            data.put("messageType", msg.getMessageType());
            data.put("content", msg.getContent());
            data.put("createdAt", msg.getCreatedAt().format(DATE_TIME_FORMATTER));

            DatabaseReference ref = getDatabase().child(COLLECTION_NAME).child(String.valueOf(msg.getConversationId() != null ? msg.getConversationId().getId() : "0"));
            DatabaseReference newRef = ref.push();
            ApiFuture<Void> writeFuture = newRef.setValueAsync(data);
            writeFuture.get();
            return msg;
        } catch (Exception e) {
            System.err.println("Failed to save chat message to Realtime Database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save chat message to Realtime Database: " + e.getMessage(), e);
        }
    }

    private ChatMessage mapDocumentToChatMessage(String documentId, Map<String, Object> data) {
        ChatMessage message = new ChatMessage();
        try {
            Object idVal = data.get("id");
            if (idVal instanceof Number) {
                message.setId(((Number) idVal).longValue());
            } else if (idVal instanceof String) {
                try {
                    message.setId(Long.parseLong((String) idVal));
                } catch (Exception ignored) {
                }
            } else {
                try {
                    message.setId(Long.parseLong(documentId));
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }

        if (data.get("senderType") instanceof String) {
            message.setSenderType((String) data.get("senderType"));
        }
        if (data.get("messageType") instanceof String) {
            message.setMessageType((String) data.get("messageType"));
        }
        if (data.get("content") instanceof String) {
            message.setContent((String) data.get("content"));
        }

        if (data.get("createdAt") instanceof String) {
            message.setCreatedAt(LocalDateTime.parse((String) data.get("createdAt"), DATE_TIME_FORMATTER));
        }

        Object conversationIdValue = data.get("conversationId");
        if (conversationIdValue instanceof Number) {
            Conversation conversation = new Conversation();
            conversation.setId(((Number) conversationIdValue).longValue());
            message.setConversationId(conversation);
        } else if (conversationIdValue instanceof String) {
            try {
                Conversation conversation = new Conversation();
                conversation.setId(Long.parseLong((String) conversationIdValue));
                message.setConversationId(conversation);
            } catch (Exception ignored) {
            }
        }

        Object senderIdValue = data.get("senderId");
        if (senderIdValue instanceof Number) {
            User sender = new User();
            sender.setId(((Number) senderIdValue).longValue());
            message.setSenderId(sender);
        } else if (senderIdValue instanceof String) {
            try {
                User sender = new User();
                sender.setId(Long.parseLong((String) senderIdValue));
                message.setSenderId(sender);
            } catch (Exception ignored) {
            }
        }

        return message;
    }
}
