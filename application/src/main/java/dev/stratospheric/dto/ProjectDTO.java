package dev.stratospheric.dto;

import dev.stratospheric.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long pid; // 프로젝트 방 번호
    private String title; // 프로젝트 방 제목
    private String category_; // 프로젝트 방 카테고리
    private String public_; // 프로젝트 방 공개여부
    private String location_; // 위치
    private String introduction; // 프로젝트 방 소개글
    private String startdate; // 프로젝트 시작 날짜
    private String enddate; // 프로젝트 종료 날짜

    private Long hostId;

    private List<MemberDto> members;

    public ProjectDTO(Project project) {

        this.pid = project.getId();
        this.title = project.getTitle();
        this.category_ = project.getCategory_();
        this.public_ = project.getPublic_();
        this.location_ = project.getLocation_();
        this.introduction = project.getIntroduction();
        this.startdate = project.getStartdate();
        this.enddate = project.getEnddate();
        this.members = project.getMembers().stream().map(MemberDto::new).collect(Collectors.toList());


    }


}
