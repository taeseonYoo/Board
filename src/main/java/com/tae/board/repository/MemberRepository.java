package com.tae.board.repository;

import com.tae.board.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<Member> findByNickName(String nickname) {
        return em.createQuery("select m from Member m where m.nickname = :nickname",Member.class)
                .setParameter("nickname",nickname)
                .getResultList();
    }
    public List<Member> findByNickNameOrEmail(String nickname,String email) {
        return em.createQuery("select m from Member m " +
                        "where m.nickname = :nickname or m.email = :email", Member.class)
                .setParameter("nickname", nickname)
                .setParameter("email", email)
                .getResultList();
    }
    public boolean existsByNickNameOrEmail(String nickname,String email) {
        return em.createQuery("select count(*) from Member m " +
                        "where m.nickname = :nickname or m.email = :email", Long.class)
                .setParameter("nickname", nickname)
                .setParameter("email", email)
                .getSingleResult() > 0;
    }

    public List<Member> findByEmail2(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    public Member findByEmail(String email) {
        return (Member) em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();
    }

}
