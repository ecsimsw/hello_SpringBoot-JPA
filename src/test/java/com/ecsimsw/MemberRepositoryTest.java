package com.ecsimsw;

import com.ecsimsw.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception{
        Member memberA = new Member();
        memberA.setUsername("memberA");

        Long saveId = memberRepository.save(memberA);
        // ctrl+alt+v

        Member found = memberRepository.find(saveId);

        Assertions.assertThat(found.getId().equals(memberA.getId()));
        Assertions.assertThat(found.getUsername().equals(memberA.getUsername()));
    }
}