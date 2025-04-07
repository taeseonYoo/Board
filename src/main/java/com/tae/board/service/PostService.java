package com.tae.board.service;


import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.dto.PageInfoDto;
import com.tae.board.exception.PostNotFoundException;
import com.tae.board.exception.UnauthorizedAccessException;
import com.tae.board.repository.MemberRepository;
import com.tae.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final int limitPage = 5;
    @Transactional
    public Long write(Long memberId, String title, String content) {
        Member member = memberRepository.findOne(memberId);
        Post post = postRepository.save(Post.createPost(member, title, content, member.getNickname()));
        return post.getId();
    }

    @Transactional
    public Long update(Long postId,Long currentMemberId,String title,String content) {
        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new PostNotFoundException("게시글이 존재하지 않습니다.");
        }
        if (!post.getMember().getId().equals(currentMemberId)) {
            throw new UnauthorizedAccessException("게시글 삭제 권한이 없습니다.");
        }

        post.updatePost(title, content);
        return post.getId();
    }
    public PageInfoDto findPagePosts(Pageable pageable) {
        Page<Post> posts = postRepository.findPostsWithPaging(pageable);

        int startPage = getStartPage(pageable);
        int endPage = getEndPage(startPage, posts.getTotalPages());

        return PageInfoDto.builder()
                .startPage(startPage)
                .endPage(endPage)
                .currentPage(pageable.getPageNumber())
                .totalPages(posts.getTotalPages())
                .posts(posts).build();

    }


    @Transactional
    public Post viewPost(Long postId) {
        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new PostNotFoundException("게시글이 존재하지 않습니다.");
        }
        post.addViewCount();
        return post;
    }

    @Transactional
    public void deletePost(Long postId,Long currentMemberId) {

        //게시물 검색
        Post post = postRepository.findOne(postId);
        if (post == null) {
            throw new PostNotFoundException("게시글을 찾을 수 없습니다.");
        }
        if (!post.getMember().getId().equals(currentMemberId)) {
            throw new UnauthorizedAccessException("게시글 삭제 권한이 없습니다.");
        }
        //멤버 연관관계 삭제
        post.removeMember();

        postRepository.delete(post);
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

    private int getStartPage(Pageable pageable) {
        return ((pageable.getPageNumber() / limitPage) * limitPage) + 1;
    }
    private int getEndPage(int startPage, int totalPages) {
        return Math.min(startPage + limitPage - 1, totalPages);
    }


}
