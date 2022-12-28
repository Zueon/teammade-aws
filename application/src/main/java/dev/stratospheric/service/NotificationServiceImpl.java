package dev.stratospheric.service;

import dev.stratospheric.dto.ApplyDto;
import dev.stratospheric.dto.NotificationDto;
import dev.stratospheric.entity.Member;
import dev.stratospheric.entity.Notification;
import dev.stratospheric.entity.Project;
import dev.stratospheric.persistence.MemberRepository;
import dev.stratospheric.persistence.NotificationRepository;
import dev.stratospheric.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
  private final ProjectRepository projectRepo;
  private final MemberRepository memberRepo;
  private final NotificationRepository notificationRepo;


  @Override
  @Transactional
  public NotificationDto notifyApplyEvent(ApplyDto applyDto) {
    Long projectId = applyDto.getPid();
    String senderEmail = applyDto.getSenderEmail();
    String content = "";

    Project project = projectRepo.findById(projectId)
      .orElseThrow(() -> new IllegalArgumentException("invalid project"));

    Member sender = memberRepo.findByEmail(senderEmail)
      .orElseThrow(() -> new IllegalArgumentException("invalid sender"));

    Member receiver = project.getHost();
    if (receiver == null) throw new IllegalArgumentException("invalid host");

    content = sender.getNickname() + "님이 프로젝트에 참가를 신청하였습니다!";

    Notification notification = Notification.builder()
      .content(content)
      .type("APPLY")
      .receiver(receiver)
      .sender(sender)
      .build();

    sender.addToSendList(notification);
    receiver.addToReceiveList(notification);
    notification = notificationRepo.save(notification);

    return Notification.toDto(notification);
  }

  @Override
  public NotificationDto notifyAcceptEvent(ApplyDto applyDto) {
    String applierEmail = applyDto.getSenderEmail();
    Long projectId = applyDto.getPid();
    String content = "";


    Project project = projectRepo.findById(projectId)
      .orElseThrow(() -> new IllegalArgumentException("invalid project"));
    Member applier = memberRepo.findByNickname(applierEmail)
      .orElseThrow(() -> new IllegalArgumentException("invalid applier"));
    Member host = project.getHost();
    if (host == null) throw new IllegalArgumentException("invalid host");



    project.addMember(applier);

    content = applier.getNickname() + "님의 프로젝트 참가 신청이 수락되었습니다!";

    Notification notification = Notification.builder()
      .content(content)
      .type("ACCEPT")
      .receiver(applier)
      .sender(host)
      .build();

    host.addToSendList(notification);
    applier.addToReceiveList(notification);
    notification = notificationRepo.save(notification);

    return Notification.toDto(notification);
  }

  private void validateApply(ApplyDto applyDto){
    String applierEmail = applyDto.getSenderEmail();
    Long projectId = applyDto.getPid();

    Project project = projectRepo.findById(projectId)
      .orElseThrow(() -> new IllegalArgumentException("invalid project"));
    Member applier = memberRepo.findByEmail(applierEmail).orElseThrow(() -> new IllegalArgumentException("invalid applier"));;

  }
}
