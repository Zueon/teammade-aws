package dev.stratospheric.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverDto {
    private Long receiverId;
    private String receiverNickname;
}
