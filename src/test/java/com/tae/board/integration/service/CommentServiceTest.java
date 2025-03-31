package com.tae.board.integration.service;

import com.tae.board.controller.form.CommentEditForm;
import com.tae.board.controller.form.MemberForm;
import com.tae.board.domain.Comments;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.exception.UnauthorizedAccessException;
import com.tae.board.service.CommentService;
import com.tae.board.service.MemberService;
import com.tae.board.service.PostService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;
    @Autowired
    EntityManager em;


    @Test
    public void 댓글_생성() {

        //given
        Member member = createMember("사용자", "USER@spring.jpa", "12345678", "USER");
        Post post = createPost(member, "댓글 생성 테스트 게시글 제목", "댓글 생성 테스트 게시글 내용");
        String content = "댓글 생성 테스트";

        //when
        Long commentId = createComments(content, member, post);
        Comments savedComment = commentService.findById(commentId);

        //then
        assertThat(savedComment.getComment()).isEqualTo(content);

        //then-연관관계 체크
        assertThat(savedComment.getMember()).isEqualTo(member);
        assertThat(savedComment.getPost()).isEqualTo(post);
        Assertions.assertTrue(post.getComments().contains(savedComment));
    }

    @Test
    public void 댓글_수정() {
        //given
        Member member = createMember("사용자", "USER@spring.jpa", "12345678", "USER");
        Post post = createPost(member, "댓글 수정 테스트 게시글 제목", "댓글 수정 테스트 게시글 내용");
        Long commentId = createComments("댓글 수정 테스트", member, post);

        //when
        String modifyContent = "내용을 변경합니다.";
        CommentEditForm commentEditForm = CommentEditForm.create(modifyContent);
        commentService.update(post.getId(), commentId, member.getId(),commentEditForm );
        Comments savedComment = commentService.findById(commentId);

        //then
        assertThat(savedComment.getComment()).isEqualTo(modifyContent);

    }

    @Test
    public void 댓글_삭제() {

        //given
        Member member = createMember("사용자", "USER@spring.jpa", "12345678", "USER");
        Post post = createPost(member, "댓글 삭제 테스트 게시글 제목", "댓글 삭제 테스트 게시글 내용");
        Long commentId = commentService.write(post.getId(), member.getId(), "댓글 삭제 테스트");

        //when
        Comments byId = commentService.findById(commentId);
        commentService.delete(commentId, post.getId(), member.getId());
        List<Comments> commentsByPost = commentService.findAllByPost(post.getId());

        //then
        assertThat(commentsByPost).isEmpty();
        assertThat(post.getComments()).isEmpty();

    }

    @Test
    public void 게시글_삭제시_댓글_삭제() {
        //given
        Member member = createMember("사용자", "USER@spring.jpa", "12345678", "USER");
        Post post = createPost(member, "댓글 삭제 테스트 게시글 제목", "댓글 삭제 테스트 게시글 내용");
        commentService.write(post.getId(), member.getId(), "댓글 삭제 테스트1");
        commentService.write(post.getId(), member.getId(), "댓글 삭제 테스트2");

        //when
        postService.deletePost(post.getId(), member.getId());

        //then
        assertThat(commentService.findAllByPost(post.getId())).isEmpty();
    }

    @Test
    public void 권한없는_댓글_삭제(){
        //given
        Member member = createMember("사용자", "USER@spring.jpa", "12345678", "USER");
        Member fake = createMember("가짜", "가짜@spring.jpa", "1234", "가짜");
        Post post = createPost(member, "제목입니다.", "내용은 무엇으로 할까요?");
        Long commentId = createComments("좋아요", member, post);

        //when & then
        assertThatThrownBy(() -> {
            commentService.delete(commentId, post.getId(), fake.getId());
        }).isInstanceOf(UnauthorizedAccessException.class)
                .hasMessage("댓글 삭제 권한이 없습니다.");

        //then
        Comments memberComment = commentService.findById(commentId);
        assertThat(memberComment).isNotNull();
        assertThat(memberComment.getPost()).isEqualTo(post);
        Assertions.assertTrue(post.getComments().contains(memberComment));

    }





    private Long createComments(String comment, Member member,Post post) {

        return commentService.write(post.getId(), member.getId(), comment);
    }

    private Member createMember(String name, String email, String password, String nickname) {
        MemberForm memberForm = new MemberForm(name, email, password, nickname);
        Long savedId = memberService.join(memberForm);
        return memberService.findOne(savedId);
    }

    private Post createPost(Member member, String title, String content) {

        Long postId = postService.write(member.getId(), title, content);
        return postService.findOne(postId);
    }

}