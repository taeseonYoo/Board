package com.tae.board.service;


import com.tae.board.domain.Comments;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.exception.PostNotFoundException;
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
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long write(Long memberId, String title,String content) {
        Member member = memberService.findOne(memberId);
        Post post = Post.createPost(member, title, content, member.getNickname());
        postRepository.save(post);
        return post.getId();
    }
    public Post findOne(Long postId) {
        return postRepository.findOne(postId);
    }
    public List<Post> findPosts() {
        return postRepository.findAll();
    }
    public List<Post> findPostsByMember(Long memberId) {
        return postRepository.findAllByMember(memberId);
    }

    @Transactional
    public Long update(Long postId,String title,String content) {
        Post findPost = postRepository.findOne(postId);
        if (findPost == null) {
            throw new PostNotFoundException("게시글이 존재하지 않습니다.");
        }
        findPost.updatePost(title, content);
        return findPost.getId();
    }
    @Transactional
    public void viewPost(Long postId) {
        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new PostNotFoundException("게시글이 존재하지 않습니다.");
        }
        post.addViewCount();
    }

    @Transactional
    public void deletePost(Long postId) {

        //게시물 검색
        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new PostNotFoundException("게시글을 찾을 수 없습니다.");
        }

        //멤버 연관관계 삭제
        post.removeMember();

        //댓글 삭제
        List<Comments> comments = post.getComments();
        for (Comments comment : comments) {
            comment.removePost();
            commentRepository.delete(comment.getId());
        }

        postRepository.delete(postId);
    }
}
