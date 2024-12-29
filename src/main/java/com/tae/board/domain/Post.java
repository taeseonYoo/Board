package com.tae.board.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(nullable = false)
    private Long view_count;

    @Column(nullable = false)
    private String writer;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comments> comments = new ArrayList<>();

    //연관 관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }
    public void addComments(Comments comments) {
        this.comments.add(comments);
        comments.setPost(this);
    }

}
