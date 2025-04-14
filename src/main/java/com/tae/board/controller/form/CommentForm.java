package com.tae.board.controller.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentForm {
    @NotEmpty
    @Size(max = 100)
    private String content;


    private CommentForm(String content) {
        this.content = content;
    }

    public static CommentForm create(String content) {
        return new CommentForm(content);
    }
}
