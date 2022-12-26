package dev.stratospheric.entity;


import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Todo extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long tid;
    private String creator;
    private String title;
    private Integer isDone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_pid")
    private Project project;
}
