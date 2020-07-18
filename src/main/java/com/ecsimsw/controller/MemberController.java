package com.ecsimsw.controller;

import com.ecsimsw.service.MemberService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.ecsimsw.domain.Address;
import com.ecsimsw.domain.Member;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm",new MemberForm());

        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result){

        // @Valid의 result를 처리하려면 BindingResult
        if(result.hasErrors()){
            return "members/createMemberForm";
            // 에러 페이지만 다시 로드해주면 Form의 @NotEmpty를 표시
            // 스프링이 알아서 BindingResult를 form에 끌고 온다.

            /*
            <label th:for="name">이름</label>
            <input type="text" th:field="*{name}" class="form-control" placeholder="이름을 입력하세요"
                   th:class="${#fields.hasErrors('name')}? 'form-control fieldError' : 'form-control'">
            <p th:if="${#fields.hasErrors('name')}" th:errors="*{name}">Incorrect date</p>
             */
        }

        Address address = new Address(
                memberForm.getCity(),
                memberForm.getStreet(),
                memberForm.getZipcode()
        );

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }
}
