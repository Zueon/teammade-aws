package dev.stratospheric.dto;

import dev.stratospheric.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Setter
@Getter
public class MemberSignupReqDto {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String address;
    private String gender;

    @Builder
    public MemberSignupReqDto(String email, String password, String name, String nickname, String address, String gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.address = address;
        this.gender = gender;
    }

    public Member toEntity(){
        return Member.builder()
                .address(address)
                .gender(gender)
                .nickname(nickname)
                .email(email)
                .password(password)
                .name(name)
                .roles(Arrays.asList("USER"))
                .build();
    }
}
