package dev.stratospheric.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenderDto {
    private Long senderId;
    private String senderNickname;
}
