package com.tae.board;

import com.tae.board.controller.form.MemberForm;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import jakarta.persistence.Column;

public class PostBuilder {
    private Member member;
    private String title="게시글 제목";
    private String content="게시글 내용";
    public static PostBuilder builder() {
        return new PostBuilder();
    }

    public PostBuilder member(Member member) {
        this.member = member;
        return this;
    }
    public PostBuilder title(String title) {
        this.title = title;
        return this;
    }
    public PostBuilder content(String content) {
        this.content = content;
        return this;
    }

    public Post build() {
        return Post.createPost(member, title, content, member.getNickname());
    }

}
