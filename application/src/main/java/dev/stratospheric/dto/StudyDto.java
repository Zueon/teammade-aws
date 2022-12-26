package dev.stratospheric.dto;

import dev.stratospheric.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyDto implements Serializable {
    private  LocalDateTime createdAt;
    private  LocalDateTime updatedAt;

    private Long sid; // 프로젝트 방 번호
    private String title; // 프로젝트 방 제목
    private String category_; // 프로젝트 방 카테고리
    private String public_; // 프로젝트 방 공개여부
    private String location_; // 위치
    private String introduction; // 프로젝트 방 소개글
    private String startdate; // 프로젝트 시작 날짜
    private String enddate; // 프로젝트 종료 날짜

    private Long hostId;

    private List<MemberDto> members;

    public StudyDto(Study study) {

        this.sid = study.getId();
        this.title = study.getTitle();
        this.category_ = study.getCategory_();
        this.public_ = study.getPublic_();
        this.location_ = study.getLocation_();
        this.introduction = study.getIntroduction();
        this.startdate = study.getStartdate();
        this.enddate = study.getEnddate();
        this.members = study.getMembers().stream().map(MemberDto::new).collect(Collectors.toList());

    }
}
