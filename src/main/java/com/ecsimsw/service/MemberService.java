package com.ecsimsw.service;

import com.ecsimsw.domain.Member;
import com.ecsimsw.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)  // JPA 데이터 변경 처리는 반드시 transaction 안에서 처리한다.
@RequiredArgsConstructor
public class MemberService  {

    /* 1. 필드 injection
    @Autowired
    private MemberRepository memberRepository;
    */


    /* 2. setter injection
    private MemberRepository memberRepository;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    // 필드로 두면 memberRepository 를 못 바꾸는데, setter로 하면 바꿀 수 있다.
    // 근데 실제로 런타임 중에 memberRepository 를 바꿀 일 이 있을까?
    // 대부분 애플리케이션 실행 시점 아닐까?
   */

    // 3. 생성자 injection
    // final로 하는 걸 추천
    public final MemberRepository memberRepository;

    /* Lombok RequiredArgsConstructor로 자동 생성
    @Autowired  // 생성자가 하나 일 경우 자동 주입이라 없어도 됨
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    */

    // 회원 가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 처리
        memberRepository.save(member);

        return member.getId();
        // DB에 쿼리 업데이트 하기전에도 영속성 컨텍스트에 저장되면(repository.save->em.persist(member))
        // 영속성 컨텍스트 캐시안의 key-value 중, key 에 pk 값으로 저장된다.
        // 따라서 id 값은 영속성 컨텍스트에 넣기만 하면 존재한다고 보장이 된다.
        // (DB에 들어간 시점이 아니더라도!)
    }

    // 중복 확인
    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원");
        }
        // 사실 이렇게 해도 동시에 같은 이름 가입이 이뤄지면 동일 이름의 회원이 생긴다.
        // 실무에선 한번 더 필터처리하거나, db 에서 유니크한 값을 갖도록 처리한다고 한다.
    }

    // 회원 전체 조회
    //@Transactional(readOnly = true) // 읽기 전용 트랜젝션, 성능 향상
                                      // 클래스 전체에 readOnly 주고, join만 디폴트로 transaction 처리
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //@Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

}
