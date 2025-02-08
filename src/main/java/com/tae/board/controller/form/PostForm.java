package com.tae.board.controller.form;

import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PostForm {

    @NotEmpty
    @Size(max = 500)
    private String title;
    @NotEmpty
    @Size(max = 1000)
    private String content;

    protected PostForm(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static PostForm from(Post post) {
        return new PostForm(post.getTitle(), post.getContent());
    }
}
