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

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String nickname;

    protected Member() {
    }

    public Member(String name, String email, String password, String nickname) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public static Member createMember(String name, String email, String password, String nickname) {
        return new Member(name, email, password, nickname);
    }

    /**
     * 양방향 연관관계를 위해 생성함.
     * mappedBy가 있는 쪽은 외래 키를 관리하지 않는다.
     * mappedBy를 통해 연관 관계의 주인을 명시한다.
     * 외래 키가 어떤 엔티티에서 관리되는지 알려준다.
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    //변경 할 수 있는 데이터는 패스워드와 닉네임뿐
    public void update(String password,String nickname){
        this.password = password;
        this.nickname = nickname;
    }

}
