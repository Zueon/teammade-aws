package dev.stratospheric.dto;

import lombok.Data;

@Data
public class MemberLoginReqDto {
    private String email;
    private String password;
}
