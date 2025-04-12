package com.tae.board.integration.service;

import com.tae.board.MemberBuilder;
import com.tae.board.controller.form.CommentEditForm;
import com.tae.board.controller.form.MemberForm;
import com.tae.board.domain.Comments;
import com.tae.board.exception.CommentNotFoundException;
import com.tae.board.exception.PostNotFoundException;
import com.tae.board.exception.UnauthorizedAccessException;
import com.tae.board.service.CommentService;
import com.tae.board.service.MemberService;
import com.tae.board.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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


    @Test
    @DisplayName("댓글 작성 성공")
    public void 댓글_생성() {

        //given
        MemberForm memberForm = MemberBuilder.builder().build();
        Long memberId = memberService.join(memberForm);
        Long postId = postService.write(memberId, "Title", "Content");

        //when
        Long commentId = commentService.write(postId, memberId, "Comment");

        //then
        Comments comment = commentService.findById(commentId);

        assertThat(comment.getComment()).isEqualTo("Comment");
        assertThat(comment.getMember().getId()).isEqualTo(memberId);
        assertThat(comment.getPost().getId()).isEqualTo(postId);
    }


    @Test
    @DisplayName("댓글 수정 성공")
    public void 댓글_수정() {
        //given
        MemberForm memberForm = MemberBuilder.builder().build();
        Long memberId = memberService.join(memberForm);
        Long postId = postService.write(memberId, "Title", "Content");
        Long commentId = commentService.write(postId, memberId, "Comment");

        //when
        Long updateCommentId = commentService.update(postId, commentId, memberId,
                CommentEditForm.create("Edit Comment"));

        //then
        Comments comment = commentService.findById(updateCommentId);
        assertThat(comment.getComment()).isEqualTo("Edit Comment");
    }

    @Test
    @DisplayName("댓글이 존재하지 않으면 수정 실패")
    public void 댓글_수정_실패1() {
        //given
        MemberForm memberForm = MemberBuilder.builder()
                .build();
        Long memberId = memberService.join(memberForm);
        Long postId = postService.write(memberId, "Title", "Content");
        Long commentId = commentService.write(postId, memberId, "Before Comment");
        commentService.delete(postId,commentId, memberId);

        //when & then
        Assertions.assertThrows(CommentNotFoundException.class,
                () -> commentService.update(postId, commentId, memberId, CommentEditForm.create("After Comment")));

        //then
        Comments comment = commentService.findById(commentId);
        assertThat(comment).isNull();
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 수정 실패")
    public void 댓글_수정_실패2() {
        //given
        MemberForm memberForm = MemberBuilder.builder().build();
        Long memberId = memberService.join(memberForm);
        Long postId = postService.write(memberId, "Title", "Content");
        Long commentId = commentService.write(postId, memberId, "Before Comment");
        postService.deletePost(postId,memberId);

        //when & then
        Assertions.assertThrows(PostNotFoundException.class,
                () -> commentService.update(postId, commentId, memberId, CommentEditForm.create("After Comment")));

        //then
        Comments comment = commentService.findById(commentId);
        assertThat(comment).isNull();
    }
    @Test
    @DisplayName("작성자 불일치 시 수정 실패")
    public void 댓글_수정_실패3() {
        //given
        MemberForm memberForm1 = MemberBuilder.builder()
                .nickname("작성자")
                .email("writer@spring.io")
                .build();
        Long memberId1 = memberService.join(memberForm1);
        MemberForm memberForm2 = MemberBuilder.builder()
                .nickname("해커")
                .email("fake@spring.io")
                .build();
        Long memberId2 = memberService.join(memberForm2);
        Long postId = postService.write(memberId1, "Title", "Content");
        Long commentId = commentService.write(postId, memberId1, "Before Comment");

        //when & then
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> commentService.update(postId, commentId, memberId2, CommentEditForm.create("After Comment")));

        //then
        Comments comment = commentService.findById(commentId);
        assertThat(comment.getComment()).isEqualTo("Before Comment");
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    public void 댓글_삭제_성공() {

        //given
        MemberForm memberForm = MemberBuilder.builder().build();
        Long memberId = memberService.join(memberForm);
        Long postId = postService.write(memberId, "Title", "Content");
        Long commentId = commentService.write(postId, memberId, "Comment");

        //when
        commentService.delete(postId, commentId, memberId);

        //then
        Comments comment = commentService.findById(commentId);
        assertThat(comment).isNull();
    }

    @Test
    @DisplayName("작성자 불일치 시 삭제 실패")
    public void 댓글_삭제_실패() {

        //given
        MemberForm memberForm1 = MemberBuilder.builder()
                .nickname("작성자")
                .email("writer@spring.io")
                .build();
        Long memberId1 = memberService.join(memberForm1);
        MemberForm memberForm2 = MemberBuilder.builder()
                .nickname("해커")
                .email("fake@spring.io")
                .build();
        Long memberId2 = memberService.join(memberForm2);
        Long postId = postService.write(memberId1, "Title", "Content");
        Long commentId = commentService.write(postId, memberId1, "Before Comment");

        //when & then
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> commentService.delete(postId, commentId, memberId2));

        //then
        Comments comment = commentService.findById(commentId);
        assertThat(comment.getComment()).isEqualTo("Before Comment");
    }


    @Test
    @DisplayName("게시글에서 생성일 기준 내림차순 정렬한 댓글 가져오기")
    public void 댓글_내림차순_가져오기() {
        //given
        MemberForm memberForm = MemberBuilder.builder().build();
        Long memberId = memberService.join(memberForm);
        Long postId = postService.write(memberId, "Title", "Content");
        for (int i = 0; i < 5; i++) {
            commentService.write(postId, memberId, "Comment" + i);
        }

        //when
        List<Comments> comments = commentService.findByPostIdOrderByCreatedDateDesc(postId);

        //then
//        assertThat(comments).isSortedAccordingTo(Comparator.comparing(Comments::getCreatedDate).reversed());
        assertThat(comments).isSortedAccordingTo(new Comparator<Comments>() {
            @Override
            public int compare(Comments o1, Comments o2) {
                if (o1.getCreatedDate().isBefore(o2.getCreatedDate())) {
                    return 1;
                } else if (o1.getCreatedDate().isEqual(o2.getCreatedDate())) {
                    return 0;
                }else{
                    return -1;
                }
            }
        });

    }
}