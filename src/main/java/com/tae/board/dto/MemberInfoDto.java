package com.tae.board.dto;

import com.tae.board.domain.Comments;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MemberInfoDto {
    private String name;
    private String email;
    private String createdDate;
    private String nickname;
    private List<Post> posts;
    private List<Comments> comments;

    protected MemberInfoDto() {
    }

    private MemberInfoDto(String name, String email, String createdDate,
                          String nickname, List<Post> posts,List<Comments> comments) {
        this.name = name;
        this.email = email;
        this.createdDate = createdDate;
        this.nickname = nickname;
        this.posts = posts;
        this.comments = comments;
    }
    public static MemberInfoDto createMemberInfo(String name, String email, String createdDate,
                                                 String nickname,List<Post> posts,List<Comments> comments){
        return new MemberInfoDto(name, email, createdDate, nickname, posts, comments);
    }


}
