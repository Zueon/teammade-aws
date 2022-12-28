package dev.stratospheric.controller;

import dev.stratospheric.dto.ApplyDto;
import dev.stratospheric.dto.NotificationDto;
import dev.stratospheric.dto.ResponseDTO;
import dev.stratospheric.entity.Member;
import dev.stratospheric.security.JwtTokenProvider;
import dev.stratospheric.service.MemberService;
import dev.stratospheric.service.NotificationService;
import dev.stratospheric.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Log4j2
@RestController
public class NotificationController {
  private final MemberService memberService;
  private final NotificationService notificationService;
  private final JwtTokenProvider tokenProvider;
  public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();


  @GetMapping("/subscribe")
  public SseEmitter subscribe(@RequestParam String token) {

    String email = tokenProvider.validateAndGetUserEmail(token);
    Member subscriber = memberService.getMemberByEmail(email);
    Long subscriberId = subscriber.getMid();

    log.info("subscriber : " + subscriberId);

    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    try {
      sseEmitter.send(SseEmitter.event().name("connect").data("connection established"));
    } catch (Exception e) {
      e.printStackTrace();
    }

    sseEmitters.put(subscriberId, sseEmitter);

    sseEmitter.onTimeout(() -> sseEmitters.remove(subscriberId));
    sseEmitter.onError((e) -> sseEmitters.remove(subscriberId));

    return sseEmitter;
  }

  @PostMapping("/apply")
  public ResponseEntity<?> apply(@RequestBody ApplyDto applyDto) {

    NotificationDto applyNote = notificationService.notifyApplyEvent(applyDto);
    Long receiverId = applyNote.getReceiverId();

    if (sseEmitters.containsKey(receiverId)) {
      SseEmitter sseEmitter = sseEmitters.get(receiverId);
      try {
        sseEmitter.send(SseEmitter.event().name("apply").data(applyNote));


      } catch (Exception e) {
        sseEmitters.remove(receiverId);
        return ResponseEntity.badRequest().body(e.getMessage());
      }
    }
    ResponseDTO response = ResponseDTO.builder().data(applyNote).build();
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/accept")
  public ResponseEntity<?> accept(@RequestBody ApplyDto applyDto){
    NotificationDto acceptNote = notificationService.notifyAcceptEvent(applyDto);
    Long receiverId = acceptNote.getReceiverId();

    if (sseEmitters.containsKey(receiverId)) {
      SseEmitter sseEmitter = sseEmitters.get(receiverId);
      try {
        sseEmitter.send(SseEmitter.event().name("accept").data(acceptNote));
      } catch (Exception e) {
        sseEmitters.remove(receiverId);
        return ResponseEntity.badRequest().body(e.getMessage());
      }
    }
    ResponseDTO response = ResponseDTO.builder().data(acceptNote).build();
    return ResponseEntity.ok().body(response);

  }

}
