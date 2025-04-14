package com.tae.board.controller.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentEditForm {

    @NotEmpty
    @Size(max = 100)
    private String content;

    private CommentEditForm(String content) {
        this.content = content;
    }

    public static CommentEditForm create(String content){
        return new CommentEditForm(content);
    }
}
