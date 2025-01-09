package com.tae.board.repository;

import com.tae.board.domain.Post;
import com.tae.board.exception.PostNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
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

        if (post == null) {
            throw new PostNotFoundException("댓글을 찾을 수 없습니다.");
        }
        post.removeMember();
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


}
