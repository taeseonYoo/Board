package com.tae.board.controller.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentForm {
    @NotEmpty
    @Size(max = 100)
    private String comment;


    private CommentForm(String comment) {
        this.comment = comment;
    }

    public static CommentForm create(String comment) {
        return new CommentForm(comment);
    }
}
