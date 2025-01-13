package com.tae.board.service;

import com.tae.board.controller.form.CommentForm;
import com.tae.board.domain.Comments;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
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
    public Long saveComment(CommentForm commentForm) {

        Post post = postRepository.findOne(commentForm.getPostId());
        Member member = memberRepository.findOne(commentForm.getMemberId());

        Comments comments = Comments.createComments(commentForm.getComment(), member, post);

        commentRepository.save(comments);
        return comments.getId();
    }
    @Transactional
    public void update(Long commentId ,CommentForm commentForm) {
        Comments findComments = commentRepository.findById(commentId);
        findComments.updateComments(commentForm.getComment());
    }

    @Transactional
    public void deleteComments(Long commentId) {
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
