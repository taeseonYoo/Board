package com.tae.board.repository;

import com.tae.board.domain.Comments;
import com.tae.board.exception.CommentNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    //댓글 작성
    public void save(Comments comments) {
        em.persist(comments);
    }

    //댓글 삭제
    public void delete(Long commentId) {
        Comments comment = em.find(Comments.class, commentId);
        em.remove(comment);
    }


    //댓글 단건조회
    public Comments findById(Long commentId) {
        return em.find(Comments.class, commentId);
    }
    //게시글 번호로 댓글 찾기
    public List<Comments> findByPost(Long postId) {
        return em.createQuery("select c from Comments c where c.post.id = :postId",Comments.class)
                .setParameter("postId", postId)
                .getResultList();
    }
    //게시자 id로 댓글 찾기
    public List<Comments> findByMember(Long memberId) {
        return em.createQuery("select c from Comments c where c.member.id = :memberId",Comments.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    /**
    * 수정 코드 N+1문제 해결
    * */
    public List<Comments> findWithMemberByPostIdOrderByCreatedDate(Long postId) {
        return em.createQuery("select c from Comments c join fetch c.member" +
                        " where c.post.id = :postId" +
                        " order by c.createdDate desc", Comments.class)
                .setParameter("postId", postId)
                .getResultList();
    }

}
