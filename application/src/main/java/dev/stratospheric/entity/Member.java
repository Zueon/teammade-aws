package dev.stratospheric.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long mid;

    @Column(unique = true)
    private String nickname;
    @Column(unique = true)
    private String email;

    private String password;
    private String name;
    private String address;
    private String gender;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @OneToOne(fetch = FetchType.LAZY)
    private Resume resume;

    @OneToMany(mappedBy = "sender")
    private List<Notification> sendList = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<Notification> receiveList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<String> roles = new ArrayList<>();


    public void addToSendList(Notification notification) {
        sendList.add(notification);
        notification.setSender(this);
    }

    public void addToReceiveList(Notification notification){
        receiveList.add(notification);
        notification.setReceiver(this);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
