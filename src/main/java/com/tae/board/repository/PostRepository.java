package com.tae.board.repository;

import com.tae.board.domain.Comments;
import com.tae.board.domain.Post;
import com.tae.board.exception.PostNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    /**
     * 게시글 저장은 persist를 사용한다
     * 수정 시에는 더티 채킹을 통하여 변경한다.
     */
    public void save(Post post) {
        em.persist(post);
    }
    //게시글 삭제
    public void delete(Long postId) {
        Post post = em.find(Post.class, postId);
        em.remove(post);
    }
    //게시글 단건 검색
    public Post findOne(Long id) {
        return em.find(Post.class, id);
    }
    //게시글 전부 검색
    public List<Post> findAll() {
        return em.createQuery("select p from Post p", Post.class).getResultList();
    }

    //사용자 별 게시글 검색
    public List<Post> findAllByMember(Long memberId) {
        return em.createQuery("select p from Post p where p.member.id =:memberId",Post.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public Page<Post> findPostsWithPaging(Pageable pageable) {
        List<Post> posts = em.createQuery("select p from Post p order by p.id desc", Post.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long totalCount = em.createQuery("select count(p) from Post p", Long.class).getSingleResult();

        return new org.springframework.data.domain.PageImpl<>(posts, pageable, totalCount);
    }


}
