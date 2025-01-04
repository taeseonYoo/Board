package com.tae.board.repository;

import com.tae.board.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA 를 사용하지 않는다.
 * Spring JPA 습득을 목표로 하기 때문에
 * EntityManager를 사용하여 직접 구현한다.
 */
@Repository
public class MemberRepository {
    @PersistenceContext
    EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /**
     * jpql 연습을 위하여
     * 닉네임으로 회원 찾기
     */
    public List<Member> findByNickName(String nickname) {

        // 결과가 없는 경우에 대한 예외처리를 하지 않았음.

        return em.createQuery("select m from Member m where m.nickname = :nickname",Member.class)
                .setParameter("nickname",nickname)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
