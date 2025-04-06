package com.tae.board.unit.service;

import com.tae.board.MemberBuilder;
import com.tae.board.PostBuilder;
import com.tae.board.controller.form.MemberForm;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.exception.PostNotFoundException;
import com.tae.board.exception.UnauthorizedAccessException;
import com.tae.board.repository.MemberRepository;
import com.tae.board.repository.PostRepository;
import com.tae.board.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;

public class PostServiceTest {
    private PostService postService;
    private final PostRepository mockPostRepo = Mockito.mock(PostRepository.class);
    private final MemberRepository mockMemberRepo = Mockito.mock(MemberRepository.class);

    @BeforeEach
    public void setUp() {
        postService = new PostService(mockPostRepo, mockMemberRepo);
    }

    @Test
    @DisplayName("게시글 작성 성공")
    public void 게시글_작성_성공() {
        //given
        Member member = MemberBuilder
                .builder()
                .build().toEntity("password");
        Post post = PostBuilder.builder()
                .member(member)
                .title("Title")
                .content("Content")
                .build();

        BDDMockito.given(mockMemberRepo.findOne(any()))
                .willReturn(member);
        BDDMockito.given(mockPostRepo.save(any()))
                .willReturn(post);
        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);

        //when
        Long postId = postService.write(1L, "Title", "Content");

        //then
        Post savedPost = postService.findOne(postId);
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("Title");
        assertThat(savedPost.getContent()).isEqualTo("Content");
    }

    @Test
    @DisplayName("게시글 제목 수정o,내용 수정o")
    public void 게시글_수정_성공() {
        //given
        Member member = MemberBuilder
                .builder()
                .build().toEntity("password");
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
    @Test
    @DisplayName("게시글 제목 수정o,내용 수정x")
    public void 게시글_제목_수정() {
        //given
        Member member = MemberBuilder
                .builder().build().toEntity("password");
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
        Long postId = postService.update(1L, 1L, "After Title", "Before Content");

        //then
        Post savedPost = postService.findOne(postId);
        assertThat(savedPost.getTitle()).isEqualTo("After Title");
        assertThat(savedPost.getContent()).isEqualTo("Before Content");
    }

    @Test
    @DisplayName("게시글 제목 수정x,내용 수정o")
    public void 게시글_내용_수정() {
        //given
        Member member = MemberBuilder
                .builder().build().toEntity("password");
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
        Long postId = postService.update(1L, 1L, "Before Title", "After Content");

        //then
        Post savedPost = postService.findOne(postId);
        assertThat(savedPost.getTitle()).isEqualTo("Before Title");
        assertThat(savedPost.getContent()).isEqualTo("After Content");
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 수정 실패")
    public void 게시글_수정_실패1() {
        //given
        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(null);

        //when & then
        Assertions.assertThrows(PostNotFoundException.class,
                () -> postService.update(1L, 1L, "After Title", "After Content"));

    }
    @Test
    @DisplayName("작성자 불일치 시 수정 실패")
    public void 게시글_수정_실패2() {
        //given
        Member member = MemberBuilder
                .builder().nickname("테스터").build()
                .toEntity("password");
        ReflectionTestUtils.setField(member, "id",1L);

        //memberId가 1인 회원이 게시글을 작성함
        Post post = PostBuilder.builder()
                .member(member)
                .build();
        ReflectionTestUtils.setField(post, "id",1L);

        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);

        /**
         * when & then
         * member_id가 2인 회원이 변경 시도
         */
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> postService.update(1L, 2L, "After Title", "After Content"));
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    public void 게시글_삭제_성공() {
        //given
        Member member = MemberBuilder.builder()
                .build().toEntity("password");
        ReflectionTestUtils.setField(member, "id",1L);

        Post post = PostBuilder.builder()
                .member(member)
                .build();
        ReflectionTestUtils.setField(post, "id",1L);

        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);

        //when
        postService.deletePost(1L, 1L);

        //then
        assertThat(member.getPosts().contains(post)).isFalse();
        BDDMockito.then(mockPostRepo).should(atLeast(1)).delete(any());
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 삭제 실패")
    public void 게시글_삭제_실패1() {
        //given
        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(null);

        //when & then
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> postService.deletePost(1L, 1L));

    }

    @Test
    @DisplayName("작성자 불일치 시 삭제 실패")
    public void 게시글_삭제_실패2() {
        //given
        Member member = MemberBuilder.builder()
                .build().toEntity("password");
        ReflectionTestUtils.setField(member, "id",1L);

        Post post = PostBuilder.builder()
                .member(member)
                .build();
        ReflectionTestUtils.setField(post, "id",1L);

        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);

        //when
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> postService.deletePost(1L, 2L));

    }



}
