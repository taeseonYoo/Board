package com.tae.board.unit.service;

import com.tae.board.MemberBuilder;
import com.tae.board.PostBuilder;
import com.tae.board.controller.form.MemberForm;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.repository.MemberRepository;
import com.tae.board.repository.PostRepository;
import com.tae.board.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class PostServiceTest {
    private PostService postService;
    private final PostRepository mockPostRepo = Mockito.mock(PostRepository.class);
    private final MemberRepository mockMemberRepo = Mockito.mock(MemberRepository.class);

    @BeforeEach
    public void setUp() {
        postService = new PostService(mockPostRepo, mockMemberRepo);
    }

    @Test
    public void 게시글_작성_성공() {
        //given
        MemberForm memberForm = MemberBuilder
                .builder()
                .nickname("테스터")
                .build();
        Post post = PostBuilder.builder()
                .member(memberForm.toEntity("password"))
                .title("게시글 제목")
                .content("게시글 내용")
                .writer(memberForm.getNickname())
                .build();
        ReflectionTestUtils.setField(post, "id",1L);

        BDDMockito.given(mockMemberRepo.findOne(any()))
                .willReturn(memberForm.toEntity("password"));
        BDDMockito.given(mockPostRepo.save(any()))
                .willReturn(post);
        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);

        //when
        Long postId = postService.write(1L, "게시글 제목", "게시글 내용");

        //then
        Post savedPost = postService.findOne(postId);
        assertThat(savedPost.getTitle()).isEqualTo("게시글 제목");
        assertThat(savedPost.getContent()).isEqualTo("게시글 내용");
        assertThat(savedPost.getWriter()).isEqualTo("테스터");
    }

    @Test
    public void 게시글_수정_성공() {
        //given
        MemberForm memberForm = MemberBuilder
                .builder()
                .nickname("테스터")
                .build();
        Member member = memberForm.toEntity("password");
        ReflectionTestUtils.setField(member, "id",1L);

        Post post = PostBuilder.builder()
                .member(member)
                .title("Before Title")
                .content("Before Content")
                .build();
        ReflectionTestUtils.setField(post, "id",1L);

        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);


        //when
        Long postId = postService.update(1L, 1L, "After Title", "After Content");

        //then
        Post savedPost = postService.findOne(postId);
        assertThat(savedPost.getTitle()).isEqualTo("After Title");
        assertThat(savedPost.getContent()).isEqualTo("After Content");
    }

}
