package com.tae.board.unit.service;

import com.tae.board.MemberBuilder;
import com.tae.board.PostBuilder;
import com.tae.board.controller.form.CommentEditForm;
import com.tae.board.domain.Comment;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.exception.CommentNotFoundException;
import com.tae.board.exception.PostNotFoundException;
import com.tae.board.exception.UnauthorizedAccessException;
import com.tae.board.repository.CommentRepository;
import com.tae.board.repository.MemberRepository;
import com.tae.board.repository.PostRepository;
import com.tae.board.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;

public class CommentServiceTest {

    private CommentService commentService;
    private final CommentRepository mockCommentRepo = Mockito.mock(CommentRepository.class);
    private final PostRepository mockPostRepo = Mockito.mock(PostRepository.class);
    private final MemberRepository mockMemberRepo = Mockito.mock(MemberRepository.class);

    @BeforeEach
    public void setUp() {
        commentService = new CommentService(mockMemberRepo, mockCommentRepo, mockPostRepo);
    }

    @Test
    @DisplayName("댓글 작성 성공")
    public void 댓글_작성_성공() {
        //given
        Member member = MemberBuilder
                .builder()
                .build().toEntity("password");
        ReflectionTestUtils.setField(member, "id", 1L);

        Post post = PostBuilder.builder()
                .member(member)
                .build();
        ReflectionTestUtils.setField(post, "id", 1L);

        Comment comment = Comment.createComments("Comment", member, post);
        ReflectionTestUtils.setField(comment, "id", 1L);

        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);
        BDDMockito.given(mockMemberRepo.findOne(any()))
                .willReturn(member);
        BDDMockito.given(mockCommentRepo.save(any()))
                .willReturn(comment);
        BDDMockito.given(mockCommentRepo.findById(any()))
                .willReturn(comment);

        //when
        Long commentId = commentService.write(1L, 1L, "Comment");

        //then
        Comment savedComment = commentService.findById(commentId);
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("Comment");
    }

    @Test
    @DisplayName("댓글 수정 성공")
    public void 댓글_수정_성공() {
        //given
        Member member = MemberBuilder
                .builder()
                .build().toEntity("password");
        ReflectionTestUtils.setField(member, "id", 1L);

        Post post = PostBuilder.builder()
                .member(member)
                .build();
        ReflectionTestUtils.setField(post, "id", 1L);

        Comment comment = Comment.createComments("Before Comment", member, post);
        ReflectionTestUtils.setField(comment, "id", 1L);

        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);
        BDDMockito.given(mockMemberRepo.findOne(any()))
                .willReturn(member);
        BDDMockito.given(mockCommentRepo.save(any()))
                .willReturn(comment);
        BDDMockito.given(mockCommentRepo.findById(any()))
                .willReturn(comment);

        //when
        Long commentId = commentService.update(1L, 1L,
                1L, CommentEditForm.create("After Comment"));

        //then
        Comment savedComment = commentService.findById(commentId);
        assertThat(savedComment.getContent()).isEqualTo("After Comment");
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 댓글 수정 실패")
    public void 댓글_수정_실패1() {
        //given
        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(null);


        //when
        assertThrows(PostNotFoundException.class,
                () -> commentService.update(1L, 1L,
                        1L, CommentEditForm.create("After Comment")));
    }

    @Test
    @DisplayName("댓글이 존재하지 않으면 댓글 수정 실패")
    public void 댓글_수정_실패2() {
        //given
        Member member = MemberBuilder
                .builder()
                .build().toEntity("password");
        Post post = PostBuilder.builder()
                .member(member)
                .build();

        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);
        BDDMockito.given(mockCommentRepo.findById(any()))
                .willReturn(null);


        //when
        assertThrows(CommentNotFoundException.class,
                () -> commentService.update(1L, 1L,
                        1L, CommentEditForm.create("After Comment")));
    }

    @Test
    @DisplayName("작성자 불일치 시 댓글 수정 실패")
    public void 댓글_수정_실패3() {
        //given
        Member member = MemberBuilder
                .builder()
                .build().toEntity("password");
        ReflectionTestUtils.setField(member, "id", 1L);

        Post post = PostBuilder.builder()
                .member(member)
                .build();

        Comment comment = Comment.createComments("Before Comment", member, post);

        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);
        BDDMockito.given(mockCommentRepo.findById(any()))
                .willReturn(comment);


        //when
        assertThrows(UnauthorizedAccessException.class,
                () -> commentService.update(1L, 1L,
                        2L, CommentEditForm.create("After Comment")));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    public void 댓글_삭제_성공() {
        //given
        Member member = MemberBuilder
                .builder()
                .build().toEntity("password");
        ReflectionTestUtils.setField(member, "id", 1L);

        Post post = PostBuilder.builder()
                .member(member)
                .build();

        Comment comment = Comment.createComments("Before Comment", member, post);

        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);
        BDDMockito.given(mockMemberRepo.findOne(any()))
                .willReturn(member);
        BDDMockito.given(mockCommentRepo.findById(any()))
                .willReturn(comment);

        //when
        commentService.delete(1L, 1L, 1L);

        //then
        assertThat(post.getComments().contains(comment)).isFalse();
        BDDMockito.then(mockCommentRepo).should(atLeast(1)).delete(any());
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 댓글 삭제 실패")
    public void 댓글_삭제_실패1() {
        //given
        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(null);


        //when
        assertThrows(PostNotFoundException.class,
                () -> commentService.delete(1L, 1L,
                        1L));
    }

    @Test
    @DisplayName("댓글이 존재하지 않으면 댓글 삭제 실패")
    public void 댓글_삭제_실패2() {
        //given
        Member member = MemberBuilder
                .builder()
                .build().toEntity("password");
        Post post = PostBuilder.builder()
                .member(member)
                .build();

        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);
        BDDMockito.given(mockCommentRepo.findById(any()))
                .willReturn(null);


        //when
        assertThrows(CommentNotFoundException.class,
                () -> commentService.delete(1L, 1L,
                        1L));
    }

    @Test
    @DisplayName("작성자 불일치 시 댓글 삭제 실패")
    public void 댓글_삭제_실패3() {
        //given
        Member member = MemberBuilder
                .builder()
                .build().toEntity("password");
        ReflectionTestUtils.setField(member, "id", 1L);

        Post post = PostBuilder.builder()
                .member(member)
                .build();

        Comment comment = Comment.createComments("Before Comment", member, post);

        BDDMockito.given(mockPostRepo.findOne(any()))
                .willReturn(post);
        BDDMockito.given(mockCommentRepo.findById(any()))
                .willReturn(comment);

        //when
        assertThrows(UnauthorizedAccessException.class,
                () -> commentService.delete(1L, 1L,
                        2L));
    }

    @Test
    @DisplayName("게시글에서 생성일 기준 내림차순 정렬한 댓글 가져오기")
    public void 댓글_내림차순_가져오기() {
        //given
        Member member = MemberBuilder
                .builder()
                .build().toEntity("password");
        ReflectionTestUtils.setField(member, "id", 1L);

        Post post = PostBuilder.builder()
                .member(member)
                .build();

        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Comment comment = Comment.createComments(i + "", member, post);
            ReflectionTestUtils.setField(comment, "createdDate", LocalDateTime.now().minusMinutes(i));
            comments.add(comment);
        }

        BDDMockito.given(mockCommentRepo.findByPostIdOrderByCreatedDateDesc(any()))
                .willReturn(comments);

        //when
        List<Comment> savedComment = commentService.findByPostIdOrderByCreatedDateDesc(1L);


        //then
        assertThat(savedComment).isSortedAccordingTo(Comparator.comparing(Comment::getCreatedDate).reversed());
    }
}
