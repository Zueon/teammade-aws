package dev.stratospheric.controller;


import dev.stratospheric.dto.*;
import dev.stratospheric.entity.Member;
import dev.stratospheric.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberAuthController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberSignupReqDto req){

        try {
            Member member = memberService.signup(req);
            MemberDto response = MemberDto.builder()
              .mid(member.getMid())
              .email(member.getEmail())
              .name(member.getName())
              .nickname(member.getNickname())
              .build();

            return ResponseEntity.ok().body(response);

        } catch (Exception  e){
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

  @PostMapping("/signin")
  public ResponseEntity<?> login(@RequestBody MemberLoginReqDto memberLoginRequestDto) {
    String email = memberLoginRequestDto.getEmail();
    String password = memberLoginRequestDto.getPassword();
    try {
      TokenInfo tokenInfo = memberService.login(email, password);
      Member member = memberService.getMemberByEmail(email);
      SigninResDTO signinResDTO = new SigninResDTO();
      signinResDTO.setAccessToken(tokenInfo.getAccessToken());
      signinResDTO.setEmail(email);
      signinResDTO.setNickname(member.getNickname());
      signinResDTO.setMid(member.getMid());

      return ResponseEntity.ok().body(signinResDTO);

    } catch (Exception e){
      ResponseDTO responseDTO = ResponseDTO.builder()
        .error(e.getMessage()).build();
      return ResponseEntity
        .badRequest()
        .body(responseDTO);
    }

  }
}
