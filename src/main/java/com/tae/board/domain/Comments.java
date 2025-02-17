package com.tae.board.domain;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Comments extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comments_id")
    private Long id;

    @Column(length = 100,nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    protected Comments() {
    }

    public Comments(String comment, Member member, Post post) {
        setPost(post);
        this.member = member;
        this.comment = comment;
    }

    public static Comments createComments(String comment, Member member, Post post) {
        return new Comments(comment, member, post);
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
    public void updateComments(String comment) {
        this.comment = comment;
    }


}
