package com.tae.board.service;


import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.exception.PostNotFoundException;
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
        postRepository.delete(postId);
    }
}
