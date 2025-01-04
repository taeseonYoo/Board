package com.tae.board.repository;

import com.tae.board.domain.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    //게시글 저장
    public void save(Post post) {
        if (post.getId() == null) {
            //아직 저장되지 않음
            em.persist(post);
        } else {
            em.merge(post);
        }
    }
    //게시글 삭제
    public void delete(Long postId) {
        Post post = em.find(Post.class, postId);

        if (post != null) {
            em.remove(post);
        }

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
