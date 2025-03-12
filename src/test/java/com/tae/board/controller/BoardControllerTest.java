package com.tae.board.controller;

import com.tae.board.domain.Comments;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.service.CommentService;
import com.tae.board.service.MemberService;
import com.tae.board.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    @Test
    @WithMockUser
    public void 테스트() throws Exception {
        Member member1 = createMember("유저1", "user1@spring.io", "12345678", "유저1");
        Member member2 = createMember("유저2", "user2@spring.io", "12345678", "유저2");
        Member member3 = createMember("유저3", "user3@spring.io", "12345678", "유저3");

        Post post = createPost(member1, "게시글 제목", "게시글 내용");
        createComments("댓글1", member1, post);
        createComments("댓글2", member2, post);
        createComments("댓글3", member3, post);

        mockMvc.perform(get("/board/post/{postId}", post.getId()))
                .andExpect(status().isOk());
    }



    private Comments createComments(String comment, Member member, Post post) {
        Long savedId = commentService.write(post.getId(), member.getId(), comment);
        return commentService.findById(savedId);
    }

    private Member createMember(String name, String email, String password, String nickname) {
        Member member = Member.createMember(name, email, password, nickname);
        Long savedId = memberService.join(member);
        return memberService.findOne(savedId);
    }

    private Post createPost(Member member, String title, String content) {
        Long savedId = postService.write(member.getId(), title, content);
        return postService.findOne(savedId);
    }
}


