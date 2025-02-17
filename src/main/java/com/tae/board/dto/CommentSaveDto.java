package com.tae.board.dto;

import lombok.Getter;

@Getter
public class CommentSaveDto {
    private String comment;
    private Long memberId;
    private Long postId;


    protected CommentSaveDto() {
    }

    private CommentSaveDto(String comment, Long memberId, Long postId) {
        this.comment = comment;
        this.memberId = memberId;
        this.postId = postId;
    }

    public static CommentSaveDto createCommentSaveDto(String comment, Long memberId, Long postId) {
        return new CommentSaveDto(comment, memberId, postId);
    }

}
