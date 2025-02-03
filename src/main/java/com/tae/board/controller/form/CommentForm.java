package com.tae.board.controller.form;

import lombok.Getter;

@Getter
public class CommentForm {

    private String comment;
    private Long postId;
    private Long memberId;

    public CommentForm() {
    }

    protected CommentForm(String comment, Long postId, Long memberId) {
        this.comment = comment;
        this.postId = postId;
        this.memberId = memberId;
    }
    public static CommentForm from(String comment, Long postId, Long memberId) {
        return new CommentForm(comment, postId, memberId);
    }
}
