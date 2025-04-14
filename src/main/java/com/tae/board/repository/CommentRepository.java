package com.tae.board.repository;

import com.tae.board.domain.Comment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    //댓글 작성
    public Comment save(Comment comment) {
        em.persist(comment);
        return comment;
    }

    //댓글 삭제
    public void delete(Long commentId) {
        Comment comment = em.find(Comment.class, commentId);
        em.remove(comment);
    }


    //댓글 단건조회
    public Comment findById(Long commentId) {
        return em.find(Comment.class, commentId);
    }
    //게시글 번호로 댓글 찾기
    public List<Comment> findByPost(Long postId) {
        return em.createQuery("select c from Comment c where c.post.id = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }
    //게시자 id로 댓글 찾기
    public List<Comment> findByMember(Long memberId) {
        return em.createQuery("select c from Comment c where c.member.id = :memberId", Comment.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    /**
    * 수정 코드 N+1문제 해결
    * */
    public List<Comment> findByPostIdOrderByCreatedDateDesc(Long postId) {
        return em.createQuery("select c from Comment c join fetch c.member" +
                        " where c.post.id = :postId" +
                        " order by c.createdDate desc", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

}
