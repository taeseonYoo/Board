package com.tae.board.controller.form;

import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PostForm {

    @NotEmpty
    private String title;
    @NotEmpty
    private String content;

    protected PostForm(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static PostForm from(Post post) {
        return new PostForm(post.getTitle(), post.getContent());
    }
}
