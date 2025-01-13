package com.tae.board.controller;

import com.tae.board.controller.form.MemberForm;
import com.tae.board.domain.Member;
import com.tae.board.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/auth")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "member/memberJoin";
    }

    @PostMapping("/auth/register")
    public String create(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/memberJoin";
        }

        Member member = Member.createMember(memberForm.getName(), memberForm.getEmail(),
                memberForm.getPassword(), memberForm.getNickname());
        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/auth/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "member/loginForm";
    }

    @PostMapping("/auth/login")
    public String login(Model model) {
        return "redirect:/";
    }
}
