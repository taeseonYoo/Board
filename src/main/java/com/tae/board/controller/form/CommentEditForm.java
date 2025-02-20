package com.tae.board.controller.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentEditForm {

    @NotEmpty
    @Size(max = 100)
    private String comment;

    private CommentEditForm(String comment) {
        this.comment = comment;
    }

    public static CommentEditForm create(String comment){
        return new CommentEditForm(comment);
    }
}
