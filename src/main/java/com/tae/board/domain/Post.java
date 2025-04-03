package com.tae.board.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(name = "view_count",nullable = false)
    private Long viewCount=0L;

    @Column(nullable = false)
    private String writer;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comments> comments = new ArrayList<>();

    protected Post() {

    }
    public Post(Member member,String title,String content,String writer){
        setMember(member);
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
    public static Post createPost(Member member,String title,String content,String writer) {
        return new Post(member,title,content,writer);
    }

    //연관 관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }

    // 연관 관계 해제 메서드
    public void removeMember() {
        this.member.getPosts().remove(this);
        this.member = null;
    }

    /**
     * 비즈니스 로직 작성
     */
    //게시글 수정
    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //조회 수 증가
    public void addViewCount() {
        this.viewCount++;
    }



}
