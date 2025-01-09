package com.tae.board.dto;

import com.tae.board.domain.Comments;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberInfoDto {
    private Long memberId;
    private String name;
    private String email;
    private String nickname;
    private List<Post> posts;
    private List<Comments> comments;

    public MemberInfoDto(Member member) {
        this.memberId = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
