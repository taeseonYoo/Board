package com.tae.board.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String email;

    private String password;

    private String nickname;

    /**
     * 양방향 연관관계를 위해 생성함.
     * mappedBy가 있는 쪽은 외래 키를 관리하지 않는다.
     * mappedBy를 통해 연관 관계의 주인을 명시한다.
     * 외래 키가 어떤 엔티티에서 관리되는지 알려준다.
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
}
