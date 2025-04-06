package com.tae.board.integration.service;

import com.tae.board.MemberBuilder;
import com.tae.board.PostBuilder;
import com.tae.board.controller.form.CommentForm;
import com.tae.board.controller.form.MemberForm;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.exception.PostNotFoundException;
import com.tae.board.exception.UnauthorizedAccessException;
import com.tae.board.repository.PostRepository;
import com.tae.board.service.MemberService;
import com.tae.board.service.PostService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("게시글 작성 성공")
    public void 게시글_작성() {
        //given
        MemberForm memberForm = MemberBuilder.builder().build();
        Long memberId = memberService.join(memberForm);

        //when
        Long postId = postService.write(memberId, "Title", "Content");

        //then
        Member member = memberService.findOne(memberId);
        Post post = postService.findOne(postId);

        assertThat(post.getTitle()).isEqualTo("Title");
        assertThat(post.getContent()).isEqualTo("Content");

        assertThat(post.getMember()).isEqualTo(member);
        assertThat(member.getPosts().contains(post)).isTrue();
    }

    @Test
    @DisplayName("게시글 수정 성공")
    public void 게시글_수정_성공() {
        //given
        MemberForm memberForm = MemberBuilder.builder().build();
        Long memberId = memberService.join(memberForm);
        Long postId = postService.write(memberId, "Before Title", "Before Content");

        //when
        Long updatePostId = postService.update(postId, memberId, "After Title", "After Content");

        //then
        Post savedPost = postService.findOne(updatePostId);
        assertThat(savedPost.getTitle()).isEqualTo("After Title");
        assertThat(savedPost.getContent()).isEqualTo("After Content");
    }
    @Test
    @DisplayName("게시글이 존재하지 않으면 수정 실패")
    public void 게시글_수정_실패1() {
        //given
        MemberForm memberForm = MemberBuilder.builder().build();
        Long memberId = memberService.join(memberForm);
        Long postId = postService.write(memberId, "Before Title", "Before Content");
        postService.deletePost(postId, memberId);

        //when & then
        Assertions.assertThrows(PostNotFoundException.class,
                () -> postService.update(postId, memberId, "After Title", "After Title"));

        //then
        Post savedPost = postService.findOne(postId);
        assertThat(savedPost).isNull();
    }
    @Test
    @DisplayName("작성자 불일치 시 수정 실패")
    public void 게시글_수정_실패2() {
        //given
        MemberForm memberForm1 = MemberBuilder.builder()
                .nickname("작성자").email("writer@spring.io").build();
        Long memberId1 = memberService.join(memberForm1);
        MemberForm memberForm2 = MemberBuilder.builder()
                .nickname("해커").email("fake@spring.io").build();
        Long memberId2 = memberService.join(memberForm2);

        Long postId = postService.write(memberId1, "Before Title", "Before Content");


        //when & then
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> postService.update(postId, memberId2, "After Title", "After Title"));

        //then
        Post savedPost = postService.findOne(postId);
        Member writer = memberService.findOne(memberId1);
        assertThat(savedPost.getTitle()).isEqualTo("Before Title");
        assertThat(savedPost.getTitle()).isEqualTo("Before Title");
        assertThat(savedPost.getMember()).isEqualTo(writer);
    }
    @Test
    @DisplayName("게시글 삭제 성공")
    public void 게시글_삭제_성공() {
        //given
        MemberForm memberForm = MemberBuilder.builder().build();
        Long memberId = memberService.join(memberForm);
        Long postId = postService.write(memberId, "Before Title", "Before Content");

        //when
        postService.deletePost(postId, memberId);

        //then
        Post savedPost = postService.findOne(postId);
        assertThat(savedPost).isNull();
    }
    @Test
    @DisplayName("게시글이 존재하지 않으면 삭제 실패")
    public void 게시글_삭제_실패1() {
        //given
        MemberForm memberForm = MemberBuilder.builder().build();
        Long memberId = memberService.join(memberForm);
        Long postId = postService.write(memberId, "Before Title", "Before Content");
        postService.deletePost(postId,memberId);

        //when & then
        Assertions.assertThrows(PostNotFoundException.class,
                () -> postService.deletePost(postId, memberId));

        //then
        Post savedPost = postService.findOne(postId);
        assertThat(savedPost).isNull();
    }

    @Test
    @DisplayName("작성자 불일치 시 삭제 실패")
    public void 게시글_삭제_실패2() {
        //given
        MemberForm memberForm1 = MemberBuilder.builder()
                .nickname("작성자").email("writer@spring.io").build();
        Long memberId1 = memberService.join(memberForm1);
        MemberForm memberForm2 = MemberBuilder.builder()
                .nickname("해커").email("fake@spring.io").build();
        Long memberId2 = memberService.join(memberForm2);

        Long postId = postService.write(memberId1, "Before Title", "Before Content");


        //when & then
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> postService.deletePost(postId, memberId2));

        //then
        Post savedPost = postService.findOne(postId);
        assertThat(savedPost).isNotNull();
    }

    @Test
    public void 조회수_증가() {
        //given
        MemberForm memberForm = MemberBuilder.builder().build();
        Long memberId = memberService.join(memberForm);

        Long postId = postService.write(memberId, "Before Title", "Before Content");

        //when
        postService.viewPost(postId);

        //then
        Post savedPost = postService.findOne(postId);
        assertThat(savedPost.getViewCount()).isEqualTo(1);
    }

}