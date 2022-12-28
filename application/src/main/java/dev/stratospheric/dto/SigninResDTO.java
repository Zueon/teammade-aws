package dev.stratospheric.dto;

import lombok.Data;

@Data
public class SigninResDTO {
    private Long mid;
    private String email;
    private String nickname;
    private String accessToken;
}
