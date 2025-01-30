package com.tae.board.controller.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PostForm {

    @NotEmpty
    private String title;
    @NotEmpty
    private String content;

    public PostForm(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
