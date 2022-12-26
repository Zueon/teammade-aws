package dev.stratospheric.service;


import dev.stratospheric.dto.MemberSignupReqDto;
import dev.stratospheric.dto.TokenInfo;
import dev.stratospheric.entity.Member;

public interface MemberService {

    TokenInfo login(String email, String password);
    Member signup(MemberSignupReqDto req);
    Member getMemberByEmail(String email);

}
