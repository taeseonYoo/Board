package com.tae.board.controller;

import com.tae.board.controller.form.PostForm;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.security.MemberDetail;
import com.tae.board.service.MemberService;
import com.tae.board.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final PostService postService;
    private final MemberService memberService;

    @GetMapping({"/", "/board"})
    public String home(@AuthenticationPrincipal MemberDetail memberDetail,Model model) {
        List<Post> posts = postService.findPosts();

        if (memberDetail != null) {
            model.addAttribute("nickname", memberDetail.getNickname());
        }

        model.addAttribute("posts", posts);
        return "board";
    }

    @GetMapping("/board/write")
    public String createForm(@ModelAttribute PostForm postForm) {

        return "post/createPostForm";
    }

    @PostMapping("/board/write")
    public String create(@AuthenticationPrincipal MemberDetail memberDetail, @Valid PostForm postForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "post/createPostForm";
        }
        postService.write(memberDetail.getMember().getId(), postForm.getTitle(), postForm.getContent());

        /**
         * 작성하고 게시글 화면으로 가는 게 좋겠다.
         */
        return "redirect:/board";
    }

}
