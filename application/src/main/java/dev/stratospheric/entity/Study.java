package dev.stratospheric.entity;

import dev.stratospheric.dto.StudyDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Setter
@Entity
public class Study extends Post{
    @OneToOne(fetch = FetchType.LAZY)
    private Member host;

    @OneToMany(mappedBy = "study")
    protected List<Member> members = new ArrayList<>();


    public List<Member> getMembers() {
        return members;
    }

    public void addMember(Member member){
        members.add(member);
        member.setStudy(this);
    }

    public void addHost(Member member){
        this.host = member;
        member.setStudy(this);
    }

    public Study() {
    }

    public Study(StudyDto dto) {

        setTitle(dto.getTitle());
        setCategory_(dto.getCategory_());
        setIntroduction(dto.getIntroduction());
        setEnddate(dto.getEnddate());
        setLocation_(dto.getLocation_());
        setPublic_(dto.getPublic_());
        setStartdate(dto.getStartdate());

    }
}
