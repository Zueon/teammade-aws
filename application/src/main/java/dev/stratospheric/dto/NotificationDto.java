package dev.stratospheric.dto;

import dev.stratospheric.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class NotificationDto implements Serializable {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long nid;
    private String type;
    private String sender;
    private Long senderId;
    private String receiver;
    private Long receiverId;
    private String content;

    public NotificationDto(final Notification entity){
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
        nid = entity.getNid();
        sender = entity.getSender().getNickname();
        senderId = entity.getSender().getMid();
        receiver = entity.getReceiver().getNickname();
        receiverId = entity.getReceiver().getMid();
        content = entity.getContent();
    }
}
