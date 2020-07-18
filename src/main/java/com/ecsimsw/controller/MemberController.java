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
import java.util.List;
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

    // Member Entity 를 Request, Response 하는 것보다 Form을 바로 받는게 더 좋다.
    // @NotEmpty 같은 Validation 표시하는 것도 있고, member.id같은 속성이 불필요할 수 도 있어서.
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

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        // Entity를 직접 뿌렸는데
        // 실제로는 dto로 transfer해서 화면단에서 필요한 데이터만 넘기는걸 추천.
        // 특히 api를 만들때는 절대 entity를 requset, response에 사용해선 안된다.

        model.addAttribute("members", members);
        return "members/memberList";
    }
}
