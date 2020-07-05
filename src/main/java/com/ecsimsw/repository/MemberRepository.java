package com.ecsimsw.repository;

import com.ecsimsw.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;


// Entity 클래스는 데이터베이스 테이블 매핑,
// repository는 Entity 조회를 위한 쿼리를 메서드화해서 사용하는 역할.

@Repository // @Component 빈 등록
@RequiredArgsConstructor
public class MemberRepository {

    /*
    // Entity manager 자동 주입
    @PersistenceContext
    private EntityManager em;
    */

    // RequiredArgsConstructor로 final 필드에 대한 생성자가 생성되고
    // 생성자가 한개 뿐이므로 파라미터가 @Autowired로 자동 주입이된다.
    // 원래는 Entity manager의 경우 @PersistenceContext로 주입하는 것이 맞지만,
    // 스프링부트에서는 특별히 @Autowired 주입이 자동 처리!
    private final EntityManager em;

    // @PersistenceUnit
    // private EntityManagerFactory emf;
    // Entity manager factory를 얻고 싶으면 PersistenceUinit으로

    // ctrl+shift+t => test
    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    //entity 객체 자체에 대한 조회는 jpql 사용
    public List<Member> findAll(){
         return em.createQuery("select m from Member m", Member.class)
                 .getResultList();
    }
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }
}
