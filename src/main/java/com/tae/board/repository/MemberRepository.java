package com.tae.board.repository;

import com.tae.board.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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

//    public boolean existsByNickNameOrEmail(String nickname,String email) {
//        return em.createQuery("select 1 from Member m " +
//                        "where m.nickname = :nickname or m.email = :email", Integer.class)
//                .setParameter("nickname", nickname)
//                .setParameter("email", email)
//                .setMaxResults(1)
//                .getResultList()
//                .isEmpty();
//    }
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    public Member findByEmail(String email) {
        try {
            return em.createQuery("select m from Member m where m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
