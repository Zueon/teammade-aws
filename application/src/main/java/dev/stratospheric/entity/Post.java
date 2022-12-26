package dev.stratospheric.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id; // 프로젝트 방 번호
    private String title; // 프로젝트 방 제목
    private String category_; // 프로젝트 방 카테고리
    private String public_; // 프로젝트 방 공개여부
    private String location_; // 위치
    private String introduction; // 프로젝트 방 소개글
    private String startdate; // 프로젝트 시작 날짜
    private String enddate; // 프로젝트 종료 날짜


}
