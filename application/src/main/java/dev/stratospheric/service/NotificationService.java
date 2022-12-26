package dev.stratospheric.service;


import dev.stratospheric.dto.ApplyDto;
import dev.stratospheric.dto.NotificationDto;

public interface NotificationService {
    NotificationDto notifyApplyEvent(ApplyDto applyDto);
    NotificationDto notifyAcceptEvent(ApplyDto applyDto);

}
