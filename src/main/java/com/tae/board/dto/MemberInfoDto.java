package com.tae.board.dto;

import com.tae.board.domain.Comment;
import com.tae.board.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MemberInfoDto {
    private String name;
    private String email;
    private LocalDateTime createdDate;
    private String nickname;
    private List<Post> posts;
    private List<Comment> comments;

    protected MemberInfoDto() {
    }

    private MemberInfoDto(String name, String email, LocalDateTime createdDate,
                          String nickname, List<Post> posts,List<Comment> comments) {
        this.name = name;
        this.email = email;
        this.createdDate = createdDate;
        this.nickname = nickname;
        this.posts = posts;
        this.comments = comments;
    }
    public static MemberInfoDto createMemberInfo(String name, String email, LocalDateTime createdDate,
                                                 String nickname,List<Post> posts,List<Comment> comments){
        return new MemberInfoDto(name, email, createdDate, nickname, posts, comments);
    }


}
