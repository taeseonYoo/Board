package com.tae.board.service;

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
    public Long write(Long postId, Long memberId, String content) {

        Post post = postRepository.findOne(postId);
        Member member = memberRepository.findOne(memberId);

        Comment comment = commentRepository.save(Comment.createComments(content, member, post));
        return comment.getId();
    }

    @Transactional
    public Long update(Long postId, Long commentId, Long currentMemberId, CommentEditForm commentEditForm) {

        Post post = postRepository.findOne(postId);
        Comment comment = commentRepository.findById(commentId);
        if (post == null) {
            throw new PostNotFoundException("게시글을 찾을 수 없습니다.");
        }
        if (comment == null) {
            throw new CommentNotFoundException("댓글을 찾을 수 없습니다.");
        }
        if (!comment.getMember().getId().equals(currentMemberId)) {
            throw new UnauthorizedAccessException("댓글 삭제 권한이 없습니다.");
        }
        comment.updateComments(commentEditForm.getContent());
        return comment.getId();
    }

    @Transactional
    public void delete(Long postId, Long commentId,Long currentMemberId) {

        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new PostNotFoundException("게시글을 찾을 수 없습니다.");
        }
        Comment comment = commentRepository.findById(commentId);
        if (comment == null) {
            throw new CommentNotFoundException("댓글을 찾을 수 없습니다.");
        }
        if (!comment.getMember().getId().equals(currentMemberId)) {
            throw new UnauthorizedAccessException("댓글 삭제 권한이 없습니다.");
        }

        comment.removePost();

        commentRepository.delete(commentId);
    }
    public List<Comment> findByPostIdOrderByCreatedDateDesc(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedDateDesc(postId);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    public List<Comment> findAllByPost(Long postId) {
        return commentRepository.findByPost(postId);
    }
    public List<Comment> findAllByMember(Long memberId) {
        return commentRepository.findByMember(memberId);
    }


}
