package com.tae.board.controller;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentForm {

    private String comment;
    private Long postId;
    private Long memberId;
}
