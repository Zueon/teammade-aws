package dev.stratospheric.dto;

import dev.stratospheric.entity.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResumeDto implements Serializable {
    private  LocalDateTime createdAt;
    private  LocalDateTime updatedAt;
    private  String rid;
    private  String name;
    private  String type;
    private String url;
    private  byte[] data;

    @Builder
    public ResumeDto(Resume resume){
        createdAt = resume.getCreatedAt();
        updatedAt = resume.getUpdatedAt();
        rid = resume.getRid();
        name = resume.getName();
        type = resume.getType();
        data = resume.getData();

    }
}
