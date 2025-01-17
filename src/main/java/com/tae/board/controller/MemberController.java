package com.tae.board.controller;

import com.tae.board.controller.form.MemberForm;
import com.tae.board.domain.Member;
import com.tae.board.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService ;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("/auth/register")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "member/memberJoin";
    }
    @GetMapping("/auth/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "member/loginForm";
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
