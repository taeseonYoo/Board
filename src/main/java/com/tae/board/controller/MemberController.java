package com.tae.board.controller;

import com.tae.board.controller.form.LoginForm;
import com.tae.board.controller.form.MemberForm;
import com.tae.board.domain.Member;
import com.tae.board.dto.MemberInfoDto;
import com.tae.board.security.MemberDetail;
import com.tae.board.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService ;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("/auth/register")
    public String createForm(@ModelAttribute MemberForm memberForm) {

        return "member/memberJoin";
    }
    @GetMapping("/auth/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {

        return "member/loginForm";
    }

    //마이 페이지 화면 가져오기
    @GetMapping("/auth/mypage")
    public String myPageForm(@AuthenticationPrincipal MemberDetail memberDetail, Model model){

        MemberInfoDto memberInfo = memberService.getMemberInfo(memberDetail.getUsername());
        model.addAttribute("memberInfo",memberInfo);

        return "member/myPageForm";
    }
    //회원가입
    @PostMapping("/auth/register")
    public String create(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/memberJoin";
        }
        String password = bCryptPasswordEncoder.encode(memberForm.getPassword());
        Member member = Member.createMember(memberForm.getName(), memberForm.getEmail(),
                password, memberForm.getNickname());
        memberService.join(member);

        return "redirect:/auth/login";
    }


}
