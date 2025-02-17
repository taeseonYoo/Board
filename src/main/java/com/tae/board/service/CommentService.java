package com.tae.board.service;

import com.tae.board.controller.form.CommentForm;
import com.tae.board.domain.Comments;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.dto.CommentSaveDto;
import com.tae.board.exception.CommentNotFoundException;
import com.tae.board.exception.PostNotFoundException;
import com.tae.board.exception.UnauthorizedAccessException;
import com.tae.board.repository.CommentRepository;
import com.tae.board.repository.MemberRepository;
import com.tae.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long saveComment(CommentSaveDto commentSaveDto) {

        Post post = postRepository.findOne(commentSaveDto.getPostId());
        Member member = memberRepository.findOne(commentSaveDto.getMemberId());

        Comments comments = Comments.createComments(commentSaveDto.getComment(), member, post);

        commentRepository.save(comments);
        return comments.getId();
    }
    @Transactional
    public void update(Long commentId ,CommentForm commentForm) {
        Comments findComments = commentRepository.findById(commentId);
        findComments.updateComments(commentForm.getComment());
    }

    @Transactional
    public void deleteComments(Long commentId, Long postId, Long currentMemberId) {

        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new PostNotFoundException("게시글을 찾을 수 없습니다.");
        }
        Comments comment = commentRepository.findById(commentId);
        if (comment == null) {
            throw new CommentNotFoundException("댓글을 찾을 수 없습니다.");
        }
        if (!comment.getMember().getId().equals(currentMemberId)) {
            throw new UnauthorizedAccessException("댓글 삭제 권한이 없습니다.");
        }

        comment.removePost();

        commentRepository.delete(commentId);
    }

    public Comments findById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    public List<Comments> findAllByPost(Long postId) {
        return commentRepository.findByPost(postId);
    }
    public List<Comments> findAllByMember(Long memberId) {
        return commentRepository.findByMember(memberId);
    }


}
