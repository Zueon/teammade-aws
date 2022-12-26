package dev.stratospheric.entity;

import dev.stratospheric.dto.NotificationDto;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long nid;

    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="receiver")
    private Member receiver;

    private String content;

    public static NotificationDto toDto(Notification notification){
        return NotificationDto.builder()
                .content(notification.getContent())
                .receiver(notification.getReceiver().getNickname())
                .receiverId(notification.getReceiver().getMid())
                .sender(notification.getSender().getNickname())
                .senderId(notification.getSender().getMid())
                .createdAt(notification.getCreatedAt())
                .type(notification.getType())
                .build();

    }
}
