package com.tae.board.domain;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(length = 100,nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    protected Comment() {
    }

    public Comment(String content, Member member, Post post) {
        setPost(post);
        this.member = member;
        this.content = content;
    }

    public static Comment createComments(String content, Member member, Post post) {
        return new Comment(content, member, post);
    }


    //연관관계 메서드
    public void setPost(Post post) {
        post.getComments().add(this);
        this.post = post;
    }
    public void removePost() {
        this.post.getComments().remove(this);
        this.post = null;
    }

    /**
     * 비즈니스 로직
     */
    public void updateComments(String content) {
        this.content = content;
    }


}
