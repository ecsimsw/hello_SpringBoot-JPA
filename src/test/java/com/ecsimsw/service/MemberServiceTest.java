package com.ecsimsw.service;

import com.ecsimsw.domain.Member;
import com.ecsimsw.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
//JUnit을 spring과 함께 실행한다.
@SpringBootTest
//SpringBoot를 띄운 상태에서 테스트를 돌린다.
@Transactional
//DB에 영향을 줄 때는 항상 transactional
//다만 test에는 Rollback(true)가 기본
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @Rollback(false) // rollback으로 commit 안되는 것 막기.
                     // 이제 rollback을 안함. db에 영향.
                     // test case에 있으면 기본적으로 rollback 처리
    public void 회원가입() throws Exception{
        // given : 뭐가 주어졌을 때
        Member member = new Member();
        member.setName("ecsimsw");

        // when : 이렇게하면
        Long savedId = memberService.join(member);

        // then : 결과가 이렇게 나와야한다
        em.flush(); // 쿼리 내보내기
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복처리() throws Exception{
        Member m1 = new Member();
        m1.setName("jinhwan");
        Member m2 = new Member();
        m2.setName("jinhwan");

        memberService.join(m1);
        memberService.join(m2);
        /*
        try {
            memberService.join(m2);
        }catch (IllegalStateException e) {
            return;
        }
        */

        fail("위 코드가 실행되면 안돼");
    }
}