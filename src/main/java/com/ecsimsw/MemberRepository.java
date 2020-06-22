package com.ecsimsw;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {
    // entity 찾아주는 역할

    // Entity manager 자동 주입
    @PersistenceContext
    private EntityManager em;

    // ctrl+shift+t => test
    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
