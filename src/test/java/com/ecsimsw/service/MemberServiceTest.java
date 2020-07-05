package com.ecsimsw.service;

import com.ecsimsw.domain.Member;
import com.ecsimsw.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        // given : 뭐가 주어졌을 때
        Member member = new Member();
        member.setName("kim");
        // when : 이렇게하면
        Long savedId = memberService.join(member);
        // then : 결과가 이렇게 나와야한다.
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복처리() throws Exception{

    }
}