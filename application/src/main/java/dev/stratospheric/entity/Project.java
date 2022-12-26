package dev.stratospheric.entity;


import dev.stratospheric.dto.ProjectDTO;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Entity
public class Project extends Post {

    @OneToOne(fetch = FetchType.LAZY)
    private Member host;

    @OneToMany(mappedBy = "project")
    protected List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    protected List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Todo> todos = new ArrayList<>();



    public void addMember(Member member){
        members.add(member);
      member.setProject(this);
    }

    public void addHost(Member member){
        this.host = member;
        member.setProject(this);
        member.setRoles(Arrays.asList("USER", "HOST"));
    }


    public void addTodo(Todo todo){
        todos.add(todo);
        todo.setProject(this);
    }


    public Project() {
    }

    public Project(ProjectDTO dto) {
        setId(dto.getPid());
        setTitle(dto.getTitle());
        setCategory_(dto.getCategory_());
        setIntroduction(dto.getIntroduction());
        setEnddate(dto.getEnddate());
        setLocation_(dto.getLocation_());
        setPublic_(dto.getPublic_());
        setStartdate(dto.getStartdate());

    }
}
